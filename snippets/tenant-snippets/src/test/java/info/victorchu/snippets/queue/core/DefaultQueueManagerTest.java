package info.victorchu.snippets.queue.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.QueueManager;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.policy.FairScheduler;

/**
 * DefaultQueueManager 单元测试 + 端到端集成测试。
 */
class DefaultQueueManagerTest {

    private ExecutorService workerPool;
    private DefaultQueueManager mgr;

    @BeforeEach
    void setUp() {
        workerPool = Executors.newFixedThreadPool(4);
        mgr = new DefaultQueueManager(workerPool);
    }

    @AfterEach
    void tearDown() {
        mgr.shutdownDrainer();
        workerPool.shutdownNow();
    }

    private static Task task(String id, String queue) {
        return new Task() {
            @Override public String id() { return id; }
            @Override public String targetQueue() { return queue; }
            @Override public int priority() { return 1; }
            @Override public long submittedAt() { return 0; }
            @Override public void execute() { }
        };
    }

    // ----------------- CRUD -----------------

    @Test
    void createQueueReturnsQueueOnFirstCreate() {
        assertNotNull(mgr.createQueue(QueueConfig.of("q1", 10, 5, new FairScheduler())));
    }

    @Test
    void createQueueWithDuplicateNameReturnsNull() {
        mgr.createQueue(QueueConfig.of("q1", 10, 5, new FairScheduler()));
        assertNull(mgr.createQueue(QueueConfig.of("q1", 20, 5, new FairScheduler())));
    }

    @Test
    void removeQueueOnMissingReturnsFalse() {
        assertFalse(mgr.removeQueue("nope"));
    }

    // ----------------- submit 路由 -----------------

    @Test
    void submitRoutesByTaskTargetQueue() {
        mgr.createQueue(QueueConfig.of("alpha", 10, 5, new FairScheduler()));
        assertTrue(mgr.submit(task("t1", "alpha")));
        assertFalse(mgr.submit(task("t2", "beta")));  // beta 不存在
    }

    @Test
    void submitToMissingQueueReturnsFalse() {
        assertFalse(mgr.submit("missing", task("t1", "missing")));
    }

    // ----------------- pollAll -----------------

    @Test
    void pollAllDispatchesAcrossQueues() {
        mgr.createQueue(QueueConfig.of("a", 10, 5, new FairScheduler()));
        mgr.createQueue(QueueConfig.of("b", 10, 5, new FairScheduler()));
        for (int i = 0; i < 5; i++) mgr.submit(task("a-" + i, "a"));
        for (int i = 0; i < 5; i++) mgr.submit(task("b-" + i, "b"));
        List<Task> got = mgr.pollAll(20);
        assertEquals(10, got.size());
    }

    // ----------------- 端到端：QueueConsumer 集成 -----------------

    @Test
    void endToEndConsumerExecutesAllTasks() throws InterruptedException {
        AtomicInteger executed = new AtomicInteger();
        mgr.createQueue(QueueConfig.of("work", 100, 4, new FairScheduler()));
        for (int i = 0; i < 50; i++) {
            final int idx = i;
            mgr.submit(new Task() {
                @Override public String id() { return "t-" + idx; }
                @Override public String targetQueue() { return "work"; }
                @Override public int priority() { return 1; }
                @Override public long submittedAt() { return idx; }
                @Override public void execute() {
                    executed.incrementAndGet();
                }
            });
        }
        QueueConsumer consumer = new QueueConsumer(mgr, 20, 16);
        consumer.start();
        // 等待所有 task 执行完
        long deadline = System.currentTimeMillis() + 3000;
        while (executed.get() < 50 && System.currentTimeMillis() < deadline) {
            Thread.sleep(20);
        }
        consumer.stop();
        assertEquals(50, executed.get());
    }

    @Test
    void endToEndRemoveQueueStopsNewTasks() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        mgr.createQueue(QueueConfig.of("q", 10, 2, new FairScheduler()));
        for (int i = 0; i < 10; i++) {
            final int idx = i;
            mgr.submit(new Task() {
                @Override public String id() { return "t-" + idx; }
                @Override public String targetQueue() { return "q"; }
                @Override public int priority() { return 1; }
                @Override public long submittedAt() { return idx; }
                @Override public void execute() {
                    try { Thread.sleep(50); } catch (InterruptedException ignored) { }
                }
            });
        }
        assertTrue(mgr.removeQueue("q"));
        // 删除后再 submit 应该失败
        assertFalse(mgr.submit(task("late", "q")));
        // q 仍存在于 map 中（running 还没归零），会异步移除
        started.countDown();
    }

    // ----------------- 动态调整 -----------------

    @Test
    void updateConfigChangesBehavior() {
        mgr.createQueue(QueueConfig.of("q", 10, 2, new FairScheduler()));
        mgr.updateConfig("q", QueueConfig.of("q", 50, 10, new FairScheduler()));
        for (int i = 0; i < 30; i++) {
            assertTrue(mgr.submit(task("t-" + i, "q")));
        }
    }
}
