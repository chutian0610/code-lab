package info.victorchu.snippets.queue.policy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.api.TaskScheduler;

/**
 * 公平调度（FIFO）。
 * <p>忽略 {@link Task#priority()}，按入队顺序消费。</p>
 * <p>内部用 {@link ArrayDeque} 实现，所有操作 O(1)。</p>
 *
 * <p><b>非线程安全</b>：由 {@link info.victorchu.snippets.queue.core.DefaultDynamicQueue} 加锁保护。
 * 每个队列应持有独立实例。</p>
 */
public class FairScheduler implements TaskScheduler {

    private final ArrayDeque<Task> deque = new ArrayDeque<>();

    @Override
    public boolean offer(Task task) {
        // ArrayDeque 不限容量；容量由 DynamicQueue.tryEnqueue 控制
        deque.offerLast(task);
        return true;
    }

    @Override
    public Task peek() {
        return deque.peekFirst();
    }

    @Override
    public Task poll() {
        return deque.pollFirst();
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public void clear() {
        deque.clear();
    }

    @Override
    public List<Task> snapshot() {
        return new ArrayList<>(deque);
    }
}
