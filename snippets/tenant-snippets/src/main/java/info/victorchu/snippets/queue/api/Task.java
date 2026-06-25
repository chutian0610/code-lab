package info.victorchu.snippets.queue.api;

/**
 * 任务抽象。
 *
 * 约定：
 * <ul>
 *   <li>{@code id} 全局唯一，用于追踪 / 取消 / 去重</li>
 *   <li>{@code targetQueue} 决定路由到哪个队列</li>
 *   <li>{@code priority} 数值越大越优先；用于 {@link TaskScheduler#poll()} 排序</li>
 *   <li>{@code submittedAt} 用于同优先级 FIFO tiebreaker</li>
 * </ul>
 */
public interface Task {

    /** 任务唯一标识 */
    String id();

    /** 目标队列名，Manager 按此字段路由 */
    String targetQueue();

    /**
     * 优先级。
     * <p>数值越大越优先：</p>
     * <ul>
     *   <li>{@link info.victorchu.snippets.queue.policy.FairScheduler} 忽略此值</li>
     *   <li>{@link info.victorchu.snippets.queue.policy.PriorityScheduler} 按此值降序</li>
     * </ul>
     * 约定 {@code priority >= 1}。
     */
    int priority();

    /** 提交时间戳（毫秒），用于 FIFO tiebreaker */
    long submittedAt();

    /** 业务执行逻辑 */
    void execute();
}
