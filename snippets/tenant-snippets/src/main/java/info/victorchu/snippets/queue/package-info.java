/**
 * 多租户动态可调队列池。
 *
 * <h2>1. 设计目标</h2>
 *
 * 解决多租户 / 多业务场景下的三个核心问题:
 * <ol>
 *   <li><b>隔离</b>:每个租户 / 业务有独立排队区,互不干扰</li>
 *   <li><b>弹性</b>:capacity / concurrency / 调度策略可运行时动态调整</li>
 *   <li><b>协同</b>:多个队列共用一份 worker 线程池,通过调度策略决定本轮给谁分多少</li>
 * </ol>
 *
 * <h2>2. 整体架构</h2>
 *
 * 三层结构,各司其职:
 * <pre>
 *  ┌──────────────────────────────────────────────────────────┐
 *  │  api/        接口层:对外契约,只描述"做什么"                  │
 *  │  ├── Task              任务抽象(id / priority / 路由)      │
 *  │  ├── QueueConfig       不可变配置(name/cap/conc/scheduler) │
 *  │  ├── DynamicQueue      动态队列接口(tryEnqueue/pollReady…)  │
 *  │  ├── TaskScheduler     队列内调度策略(Fair/Priority)        │
 *  │  ├── QueueDispatchPolicy 跨队列调度策略(Fifo/RR/Backlog)   │
 *  │  ├── DispatchPlan      跨队列调度计划                       │
 *  │  └── QueueManager      队列管理器(增删改查 + 路由 + 拉取)   │
 *  ├──────────────────────────────────────────────────────────┤
 *  │  core/        实现层:具体怎么落地                            │
 *  │  ├── DefaultDynamicQueue  锁保护 + 动态配置 + 调度器迁移    │
 *  │  ├── DefaultQueueManager  路由 + 共享 worker 池 + 异步排空  │
 *  │  └── QueueConsumer        单线程周期 tick + 耗尽拉取        │
 *  ├──────────────────────────────────────────────────────────┤
 *  │  policy/      策略层:可插拔实现                              │
 *  │  ├── FairScheduler / PriorityScheduler    队列内调度        │
 *  │  └── FifoDispatchPolicy / RoundRobinDispatchPolicy /       │
 *  │      BacklogDispatchPolicy                跨队列调度        │
 *  └──────────────────────────────────────────────────────────┘
 * </pre>
 *
 * <h2>3. 关键概念</h2>
 *
 * <h3>3.1 容量 vs 并发</h3>
 *
 * 每个队列有两个独立上限:
 * <ul>
 *   <li><b>capacity</b>:排队区容量,限制 {@code waitingSize} 上限;
 *       满了就 {@code tryEnqueue=false}</li>
 *   <li><b>concurrency</b>:执行区并发,限制 {@code runningSize} 上限;
 *       满了就 {@code pollReady=[]}</li>
 * </ul>
 *
 * <h3>3.2 队列内调度 vs 跨队列调度</h3>
 *
 * 这是两套独立的"优先级机制",常被混淆:
 * <ul>
 *   <li><b>{@link info.victorchu.snippets.queue.api.TaskScheduler}</b>
 *       决定"从一个队列里 <i>挑哪个</i> task"(由队列的 storage 实现)</li>
 *   <li><b>{@link info.victorchu.snippets.queue.api.QueueDispatchPolicy}</b>
 *       决定"先访问 <i>哪个队列</i>、各拿 <i>多少</i>"(由 Manager 委派)</li>
 * </ul>
 *
 * 组合示例:高优租户用 {@code PriorityScheduler} 保证自己内部出队快 +
 * {@code BacklogDispatchPolicy} 保证跨租户也按积压优先派发。
 *
 * <h2>4. 线程模型</h2>
 *
 * <pre>
 *   ┌────────────┐  scheduleWithFixedDelay   ┌────────────────────┐
 *   │ 调度线程   │ ────────────────────────► │ QueueConsumer.tick │
 *   │ (单线程)   │                            └────────┬───────────┘
 *   └────────────┘                                     │ pollAll(batch)
 *                                                      ▼
 *                                          ┌────────────────────────┐
 *                                          │ QueueManager           │
 *                                          │   ↓ 委派 plan()        │
 *                                          │ QueueDispatchPolicy    │
 *                                          └────────┬───────────────┘
 *                                                   │ 各队列 pollReady
 *                                                   ▼
 *                                          ┌────────────────────────┐
 *                                          │ DefaultDynamicQueue[]  │
 *                                          │   (ReentrantLock)      │
 *                                          └────────┬───────────────┘
 *                                                   │ List&lt;Task&gt;
 *                                                   ▼
 *   ┌────────────────┐  workerPool.execute  ┌────────────────────┐
 *   │ worker 线程池  │ ◄────────────────── │ runTask → execute() │
 *   │ (多线程共享)   │                      │           finally   │
 *   └────────────────┘                      └─────────┬──────────┘
 *                                                   │ markFinished
 *                                                   ▼
 *                                          runningSize -1,槽位归还
 * </pre>
 *
 * <b>关键约束</b>:
 * <ul>
 *   <li>调度线程 <b>单</b>:避免 tick 自身并发,通过
 *       {@code scheduleWithFixedDelay}(等上次结束再等 interval)保证</li>
 *   <li>worker 线程池 <b>共享</b>:所有队列复用一份,无界或按业务容量调</li>
 *   <li>单队列内 <b>一把锁</b> ({@code ReentrantLock}):
 *       保护 waiting 状态 + scheduler 调用 + 动态配置写</li>
 *   <li>{@code runningSize} 用 {@code AtomicInteger}:
 *       pollReady 内 +1,markFinished 内 -1,无锁读</li>
 *   <li>scheduler 字段 {@code volatile}:updateScheduler 持锁换引用,无锁读</li>
 * </ul>
 *
 * <h2>5. QueueConsumer.tick 耗尽语义</h2>
 *
 * 生产 {@link info.victorchu.snippets.queue.core.QueueConsumer#tick()} 是循环:
 * <pre>
 *   while (tickOnce()) { }  // 一直 poll,直到拿不到 task
 * </pre>
 *
 * <b>为什么需要循环?</b>
 * <ul>
 *   <li>tick 内部仅把 task 丢到 worker 池(<i>不阻塞</i>)</li>
 *   <li>worker 异步 markFinished 会慢慢释放 concurrency 槽位</li>
 *   <li>循环能"吃到"这些新释放的槽位,本轮把能派的都派完</li>
 * </ul>
 *
 * <b>退出条件</b>(由 {@link info.victorchu.snippets.queue.core.QueueConsumer#tickOnce()}
 * 返回 false 触发):
 * <ul>
 *   <li>所有队列都空了</li>
 *   <li>或所有队列的 concurrency 槽位被占满,且没有正在 markFinished 的 task</li>
 * </ul>
 *
 * <b>不会死循环</b>:tick 自身不释放槽位;槽位占满时 pollAll 拿 0 个,自然退出。
 *
 * <h2>6. 典型使用流程</h2>
 *
 * <pre>{@code
 * // 1) 准备 manager + worker 池
 * ExecutorService pool = Executors.newFixedThreadPool(16);
 * DefaultQueueManager mgr = new DefaultQueueManager(pool, new BacklogDispatchPolicy());
 *
 * // 2) 创建租户队列
 * mgr.createQueue(QueueConfig.of("tenant-a", 1000, 8, new PriorityScheduler()));
 * mgr.createQueue(QueueConfig.of("tenant-b", 500, 4, new FairScheduler()));
 *
 * // 3) 启动消费者
 * QueueConsumer consumer = new QueueConsumer(mgr, 20, 32);
 * consumer.start();
 *
 * // 4) 提交 task——按 targetQueue 路由
 * mgr.submit(new MyTask("t-1", "tenant-a", 5, System.currentTimeMillis()));
 *
 * // 5) 动态调整
 * mgr.getQueue("tenant-a").updateConcurrency(16);
 *
 * // 6) 关闭
 * consumer.stop();
 * mgr.shutdownDrainer();
 * pool.shutdown();
 * }</pre>
 *
 * <h2>7. 关键不变量</h2>
 *
 * <ul>
 *   <li><b>sum(permits) == totalPermit</b>:所有跨队列调度策略满足</li>
 *   <li><b>runningSize ≤ concurrency</b>:pollReady 内 check-then-act 原子</li>
 *   <li><b>waitingSize ≤ capacity</b>:tryEnqueue 内 check-then-act 原子</li>
 *   <li><b>scheduler 调用全部在 queue lock 内</b>:实现可非线程安全</li>
 * </ul>
 *
 * <h2>8. 已知 trade-off / 限制</h2>
 *
 * <ul>
 *   <li><b>task 异常被吞</b>:snippet 阶段不引入死信 / 重试;生产应替换 catch 块</li>
 *   <li><b>调度策略访问锁内 scheduler</b>:高并发下排队区锁可能成为瓶颈;
 *       真生产可考虑 lock-free 队列 + 单独并发控制</li>
 *   <li><b>BacklogDispatchPolicy 退化分支</b>:全部队列 waiting=0 时退化为 FIFO,
 *       此时调度顺序与积压无关</li>
 *   <li><b>updateScheduler 迁移</b>:用 snapshot + 重新 offer 实现,
 *       容量受限时新 scheduler 可能拒收 task(被静默丢弃)</li>
 *   <li><b>异步排空</b>:removeQueue 不阻塞,实际移除由 drainExecutor 调度</li>
 * </ul>
 */
package info.victorchu.snippets.queue;
