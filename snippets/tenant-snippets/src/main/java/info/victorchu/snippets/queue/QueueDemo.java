package info.victorchu.snippets.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.QueueManager;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.core.DefaultQueueManager;
import info.victorchu.snippets.queue.core.QueueConsumer;
import info.victorchu.snippets.queue.policy.BacklogDispatchPolicy;
import info.victorchu.snippets.queue.policy.FairScheduler;
import info.victorchu.snippets.queue.policy.PriorityScheduler;
import info.victorchu.snippets.queue.policy.RoundRobinDispatchPolicy;

/**
 * 端到端冒烟 demo。
 *
 * <p>演示内容：</p>
 * <ol>
 *   <li>创建 Manager + 两种策略队列（Fair / Priority）</li>
 *   <li>启动统一消费者</li>
 *   <li>批量提交 task</li>
 *   <li>动态调整 capacity / concurrency</li>
 *   <li>动态切换调度策略</li>
 *   <li>删除队列（异步排空）</li>
 *   <li>使用 BacklogDispatchPolicy 替换默认 FIFO dispatch</li>
 * </ol>
 */
public class QueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1) 准备 worker pool（16 线程共享）
        ExecutorService workerPool = Executors.newFixedThreadPool(16);
        // 2) 创建 Manager，使用 BacklogDispatchPolicy 跨队列调度
        QueueManager manager = new DefaultQueueManager(workerPool, new BacklogDispatchPolicy());

        // 3) 创建2个不同策略的队列
        manager.createQueue(QueueConfig.of("order", 100, 5, new FairScheduler()));
        manager.createQueue(QueueConfig.of("payment", 50, 3, new PriorityScheduler()));
        System.out.println("[demo] 3 queues created");

        // 4) 启动消费者（每 50ms 拉一轮，每轮最多 32 个）
        QueueConsumer consumer = new QueueConsumer(manager, 50, 32);
        consumer.start();
        System.out.println("[demo] consumer started");

        // 5) 提交一批 task
        AtomicInteger orderExecuted = new AtomicInteger();
        AtomicInteger paymentExecuted = new AtomicInteger();
        AtomicInteger recommendExecuted = new AtomicInteger();
        long now = System.currentTimeMillis();

        for (int i = 0; i < 30; i++) {
            final int idx = i;
            manager.submit(new DemoTask("order-" + i, "order", 1, now + i, () -> {
                orderExecuted.incrementAndGet();
                System.out.println("[order] idx=" + idx + " thread=" + Thread.currentThread().getName());
                try {
                    Thread.sleep(20);  // 模拟业务耗时
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        for (int i = 0; i < 20; i++) {
            final int idx = i;
            int priority = (i % 5) + 1;  // priority 范围 1..5
            manager.submit(new DemoTask("pay-" + i, "payment", priority, now + i, () -> {
                paymentExecuted.incrementAndGet();
                System.out.println("[payment] prio=" + priority + " idx=" + idx);
            }));
        }

        for (int i = 0; i < 50; i++) {
            final int idx = i;
            int weight = ((i * 7) % 5) + 1;
            manager.submit(new DemoTask("rec-" + i, "recommend", weight, now + i, () -> {
                recommendExecuted.incrementAndGet();
                // recommend 队列不打印，避免日志过多
            }));
        }
        System.out.println("[demo] 100 tasks submitted");

        // 6) 动态调整：order 队列扩容 + 提高并发
        Thread.sleep(300);
        System.out.println("--- 动态调整 order: capacity 200, concurrency 10 ---");
        manager.updateConfig("order", QueueConfig.of("order", 200, 10, new FairScheduler()));

        // 8) 演示删除队列
        Thread.sleep(300);
        System.out.println("--- 删除 recommend 队列 ---");
        manager.removeQueue("recommend");

        // 9) 等待所有 task 跑完
        Thread.sleep(2000);
        consumer.stop();
        workerPool.shutdown();
        workerPool.awaitTermination(5, TimeUnit.SECONDS);

        // 10) 总结
        System.out.println("=== demo summary ===");
        System.out.println("order executed: " + orderExecuted.get() + " / 30");
        System.out.println("payment executed: " + paymentExecuted.get() + " / 20");
        System.out.println("recommend executed: " + recommendExecuted.get() + " / 50");
        System.out.println("queues remaining: " + manager.queues().size());
    }

    /**
     * 简单的 Task 实现。手写以避免 lombok @Value 生成 getXxx() 与接口方法签名不匹配。
     */
    static class DemoTask implements Task {
        private final String id;
        private final String targetQueue;
        private final int priority;
        private final long submittedAt;
        private final Runnable body;

        DemoTask(String id, String targetQueue, int priority, long submittedAt, Runnable body) {
            this.id = id;
            this.targetQueue = targetQueue;
            this.priority = priority;
            this.submittedAt = submittedAt;
            this.body = body;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public String targetQueue() {
            return targetQueue;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public long submittedAt() {
            return submittedAt;
        }

        @Override
        public void execute() {
            body.run();
        }
    }

    /**
     * 抑制 "unused import" 警告：仅用于演示多种 dispatch policy 存在。
     */
    @SuppressWarnings("unused")
    private static void touchImports() {
        RoundRobinDispatchPolicy.class.getName();
    }
}
