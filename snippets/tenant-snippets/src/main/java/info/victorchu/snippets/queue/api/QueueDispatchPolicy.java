package info.victorchu.snippets.queue.api;

import java.util.Collection;

/**
 * 跨队列调度策略（Manager 层）。
 *
 * <p>消费者从 Manager 拉取 task 时，policy 决定：</p>
 * <ul>
 *   <li>访问各队列的顺序</li>
 *   <li>每个队列本轮的拉取额度</li>
 * </ul>
 *
 * <p>与 {@link TaskScheduler} 的关系：</p>
 * <ul>
 *   <li>{@link TaskScheduler} 决定"从一个队列里挑哪个 task"</li>
 *   <li>{@link QueueDispatchPolicy} 决定"先访问哪个队列、各拿多少"</li>
 * </ul>
 */
public interface QueueDispatchPolicy {

    /**
     * 规划本轮的拉取方案。
     *
     * @param queues      当前所有活跃队列
     * @param totalPermit 本轮总拉取额度上限（消费者可指定）
     * @return 包含访问顺序与每队列额度的计划
     */
    DispatchPlan plan(Collection<? extends DynamicQueue> queues, int totalPermit);
}
