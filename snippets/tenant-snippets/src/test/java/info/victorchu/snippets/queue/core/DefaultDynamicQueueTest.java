package info.victorchu.snippets.queue.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.policy.FairScheduler;
import info.victorchu.snippets.queue.policy.PriorityScheduler;

/**
 * DefaultDynamicQueue 单元测试。
 */
class DefaultDynamicQueueTest {

    private static Task task(String id, String q, int priority) {
        return new Task() {
            @Override public String id() { return id; }
            @Override public String targetQueue() { return q; }
            @Override public int priority() { return priority; }
            @Override public long submittedAt() { return 0; }
            @Override public void execute() { }
        };
    }

    @Test
    void tryEnqueueRespectsCapacity() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 2, 5, new FairScheduler()));
        assertTrue(q.tryEnqueue(task("a", "q", 1)));
        assertTrue(q.tryEnqueue(task("b", "q", 1)));
        assertFalse(q.tryEnqueue(task("c", "q", 1)));  // 容量满
        assertEquals(2, q.waitingSize());
    }

    @Test
    void closeRejectsNewTasks() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 5, new FairScheduler()));
        q.close();
        assertFalse(q.tryEnqueue(task("a", "q", 1)));
        assertTrue(q.isClosed());
    }

    @Test
    void pollReadyRespectsConcurrency() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 2, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 1));
        q.tryEnqueue(task("b", "q", 1));
        q.tryEnqueue(task("c", "q", 1));
        // concurrency=2，一次取 5 个也只给 2 个
        List<Task> got = q.pollReady(5);
        assertEquals(2, got.size());
        assertEquals(2, q.runningSize());
        assertEquals(1, q.waitingSize());
    }

    @Test
    void markFinishedRestoresConcurrency() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 1, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 1));
        List<Task> got = q.pollReady(1);
        assertEquals(1, q.runningSize());
        q.markFinished(got.get(0));
        assertEquals(0, q.runningSize());
        // 现在可以再取
        List<Task> got2 = q.pollReady(1);
        assertEquals(0, got2.size());  // waiting 没了
    }

    @Test
    void updateCapacityTakesEffectImmediately() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 2, 5, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 1));
        q.tryEnqueue(task("b", "q", 1));
        assertFalse(q.tryEnqueue(task("c", "q", 1)));  // 容量 2 满
        q.updateCapacity(5);
        assertTrue(q.tryEnqueue(task("c", "q", 1)));
        assertEquals(3, q.waitingSize());
    }

    @Test
    void updateConcurrencyTakesEffect() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 1, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 1));
        q.tryEnqueue(task("b", "q", 1));
        assertEquals(1, q.pollReady(5).size());
        assertEquals(1, q.runningSize());
        q.updateConcurrency(2);
        assertEquals(1, q.pollReady(5).size());  // 再拿 1 个
        assertEquals(2, q.runningSize());
    }

    @Test
    void updateSchedulerMigratesWaitingTasks() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 5, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 5));
        q.tryEnqueue(task("b", "q", 1));
        // 切换为 PriorityScheduler（高 priority 先出）
        q.updateScheduler(new PriorityScheduler());
        List<Task> got = q.pollReady(10);
        assertEquals(2, got.size());
        assertEquals("a", got.get(0).id());  // priority=5 优先
        assertEquals("b", got.get(1).id());
    }

    @Test
    void configReturnsCurrentSnapshot() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 3, new FairScheduler()));
        assertEquals(10, q.config().capacity());
        assertEquals(3, q.config().concurrency());
        q.updateCapacity(20);
        q.updateConcurrency(7);
        assertEquals(20, q.config().capacity());
        assertEquals(7, q.config().concurrency());
    }

    @Test
    void pollReadyOnClosedQueueReturnsEmpty() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 5, new FairScheduler()));
        q.tryEnqueue(task("a", "q", 1));
        q.close();
        assertTrue(q.pollReady(5).isEmpty());
    }

    @Test
    void markFinishedNeverUnderflows() {
        DefaultDynamicQueue q = new DefaultDynamicQueue(
                QueueConfig.of("q", 10, 5, new FairScheduler()));
        q.markFinished(task("ghost", "q", 1));  // 多余归还不报错
        assertEquals(0, q.runningSize());
        assertNotNull(q);
    }
}
