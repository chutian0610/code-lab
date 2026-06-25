package info.victorchu.snippets.queue.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.api.TaskScheduler;

/**
 * 队列内调度策略测试（FairScheduler / PriorityScheduler）。
 */
class SchedulerTest {

    /** 简单 Task 测试桩 */
    private static Task task(String id, int priority, long submittedAt) {
        return new Task() {
            @Override public String id() { return id; }
            @Override public String targetQueue() { return "q"; }
            @Override public int priority() { return priority; }
            @Override public long submittedAt() { return submittedAt; }
            @Override public void execute() { /* noop */ }
        };
    }

    // ----------------- FairScheduler -----------------

    @Test
    void fairSchedulerFifoOrder() {
        FairScheduler s = new FairScheduler();
        s.offer(task("a", 5, 1));
        s.offer(task("b", 1, 2));   // 高 priority 也应后出（FIFO）
        s.offer(task("c", 99, 3));
        assertEquals("a", s.poll().id());
        assertEquals("b", s.poll().id());
        assertEquals("c", s.poll().id());
        assertNull(s.poll());
    }

    @Test
    void fairSchedulerSnapshotAndClear() {
        FairScheduler s = new FairScheduler();
        s.offer(task("a", 1, 1));
        s.offer(task("b", 1, 2));
        List<Task> snap = s.snapshot();
        assertEquals(2, snap.size());
        s.clear();
        assertEquals(0, s.size());
    }

    // ----------------- PriorityScheduler -----------------

    @Test
    void prioritySchedulerDescendingByPriority() {
        PriorityScheduler s = new PriorityScheduler();
        s.offer(task("low", 1, 1));
        s.offer(task("high", 5, 2));
        s.offer(task("mid", 3, 3));
        assertEquals("high", s.poll().id());
        assertEquals("mid", s.poll().id());
        assertEquals("low", s.poll().id());
    }

    @Test
    void prioritySchedulerFifoTiebreaker() {
        PriorityScheduler s = new PriorityScheduler();
        s.offer(task("first", 5, 1));
        s.offer(task("second", 5, 2));
        s.offer(task("third", 5, 3));
        assertEquals("first", s.poll().id());
        assertEquals("second", s.poll().id());
        assertEquals("third", s.poll().id());
    }

    @Test
    void prioritySchedulerPeekDoesNotRemove() {
        TaskScheduler s = new PriorityScheduler();
        s.offer(task("a", 2, 1));
        assertEquals("a", s.peek().id());
        assertEquals(1, s.size());
    }
}
