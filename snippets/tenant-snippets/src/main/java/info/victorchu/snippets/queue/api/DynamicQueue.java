package info.victorchu.snippets.queue.api;

/**
 * 动态可调整队列。
 *
 * <p>职责：</p>
 * <ul>
 *   <li>控制"排队数"（capacity，waiting 区容量）</li>
 *   <li>控制"执行数"（concurrency，并发上限）</li>
 *   <li>支持动态调整 capacity / concurrency / scheduler</li>
 *   <li>支持关闭：拒绝新 task，running 归零后由 Manager 移除</li>
 * </ul>
 *
 * <p>线程安全：所有方法须线程安全。</p>
 */
public interface DynamicQueue {

    String name();

    /** 当前生效配置 */
    QueueConfig config();

    /**
     * 入队。
     *
     * @return true 成功；false 失败（容量已满 / 队列已关闭）
     */
    boolean tryEnqueue(Task task);

    /**
     * 消费者调用：按 {@code concurrency - runningSize} 批量拿取 task。
     *
     * @param permit 本次最多取多少个
     * @return 取出的 task 列表（元素数 ≤ permit，可能为空）
     */
    java.util.List<Task> pollReady(int permit);

    /**
     * 任务执行完（或失败）后归还并发额度。
     */
    void markFinished(Task task);

    /**
     * 动态调整容量。
     * <ul>
     *   <li>扩容：立即生效</li>
     *   <li>缩容：已等待的 task 不动，新的入队请求被拒绝直到 waitingSize &lt; 新 capacity</li>
     * </ul>
     */
    void updateCapacity(int newCapacity);

    /**
     * 动态调整并发数。
     * <ul>
     *   <li>扩容：下一轮 poll 即可多拿</li>
     *   <li>缩容：runningSize 超过新值时不再派发新任务，正在跑的 task 自然结束</li>
     * </ul>
     */
    void updateConcurrency(int newConcurrency);

    /**
     * 动态切换调度策略。
     * <p>迁移逻辑：snapshot 旧 scheduler 的 task → 逐个 offer 到新 scheduler。</p>
     */
    void updateScheduler(TaskScheduler newScheduler);

    /**
     * 关闭队列：拒绝新 task 入队；已派发但未完成的 task 自然结束；
     * running 归零后由 Manager 回收。
     */
    void close();

    /** 等待中的 task 数 */
    int waitingSize();

    /** 正在执行的 task 数 */
    int runningSize();

    /** 是否已关闭 */
    boolean isClosed();
}
