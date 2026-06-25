package info.victorchu.snippets.queue.policy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.api.TaskScheduler;

/**
 * 优先级调度。
 * <p>按 {@link Task#priority()} 降序取队首；同优先级按 {@link Task#submittedAt()} 升序（FIFO tiebreaker）。</p>
 * <p>内部用 {@link PriorityQueue} 实现，offer/poll O(log n)。</p>
 *
 * <p><b>非线程安全</b>：由 {@link info.victorchu.snippets.queue.core.DefaultDynamicQueue} 加锁保护。
 * 每个队列应持有独立实例。</p>
 */
public class PriorityScheduler implements TaskScheduler {

    /** 排序：priority 降序，submittedAt 升序 */
    private static final Comparator<Task> COMPARATOR = Comparator
            .comparingInt(Task::priority)
            .reversed()                              // 数值大的优先
            .thenComparingLong(Task::submittedAt);   // 同优先级早提交优先

    private final PriorityQueue<Task> heap = new PriorityQueue<>(COMPARATOR);

    @Override
    public boolean offer(Task task) {
        return heap.offer(task);
    }

    @Override
    public Task peek() {
        return heap.peek();
    }

    @Override
    public Task poll() {
        return heap.poll();
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public void clear() {
        heap.clear();
    }

    @Override
    public List<Task> snapshot() {
        // PriorityQueue 不保证迭代顺序，这里复制为新列表保持实现细节对外透明
        return new ArrayList<>(heap);
    }
}
