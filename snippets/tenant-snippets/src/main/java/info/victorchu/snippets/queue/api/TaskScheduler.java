package info.victorchu.snippets.queue.api;

import java.util.List;

/**
 * 任务调度策略（Queue-managed 模式）。
 *
 * <p>实现类内部自行维护数据结构（数组、堆、链表等），{@link DynamicQueue} 仅作为其外壳。
 * 这种设计使得 {@link Task} 的 {@code priority} 字段在不同策略下有不同语义：</p>
 * <ul>
 *   <li>Fair：忽略 priority，FIFO</li>
 *   <li>Priority：按 priority 降序，submittedAt 升序 tiebreaker</li>
 * </ul>
 *
 * <p>所有方法须线程安全，因为 {@link DynamicQueue} 在并发环境下调用。</p>
 */
public interface TaskScheduler {

    /**
     * 入存储。
     *
     * @param task 任务
     * @return true 成功；false 存储拒绝（容量受限等情况）
     */
    boolean offer(Task task);

    /**
     * 查看下一个待执行任务，不删除。
     *
     * @return 下一个 task；存储为空时返回 null
     */
    Task peek();

    /**
     * 取并删除下一个待执行任务。
     *
     * @return 取出的 task；存储为空时返回 null
     */
    Task poll();

    /**
     * 当前等待中的任务数。
     */
    int size();

    /**
     * 清空存储（队列关闭时调用）。
     */
    void clear();

    /**
     * 当前存储内容的只读快照。
     * <p>用途：</p>
     * <ul>
     *   <li>动态切换策略时迁移 task</li>
     *   <li>监控 / 调试</li>
     * </ul>
     */
    List<Task> snapshot();
}
