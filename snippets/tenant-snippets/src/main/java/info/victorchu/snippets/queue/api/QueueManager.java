package info.victorchu.snippets.queue.api;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 队列管理器。
 *
 * <p>职责：</p>
 * <ul>
 *   <li>管理所有队列的增删改查</li>
 *   <li>task 提交路由（按 {@link Task#targetQueue()}）</li>
 *   <li>消费者统一拉取入口（按 {@link QueueDispatchPolicy} 跨队列调度）</li>
 *   <li>持有全局共享执行线程池</li>
 * </ul>
 */
public interface QueueManager {

    /** 创建队列。队列名已存在时返回 null 或抛异常（由实现决定） */
    DynamicQueue createQueue(QueueConfig config);

    /**
     * 移除队列。
     * <p>异步：调用后立即标记为关闭，由后台任务在 running 归零后真正移除。</p>
     *
     * @return true 找到并标记关闭；false 队列不存在
     */
    boolean removeQueue(String name);

    /** 获取队列；不存在返回 null */
    DynamicQueue getQueue(String name);

    /** 队列是否存在 */
    boolean exists(String name);

    /**
     * 动态更新队列配置。
     * <p>仅更新 capacity / concurrency / scheduler，name 字段被忽略（用以校验）。</p>
     */
    void updateConfig(String name, QueueConfig newConfig);

    /**
     * 提交 task，按 {@link Task#targetQueue()} 路由。
     *
     * @return true 入队成功；false 失败（队列不存在 / 容量满 / 已关闭）
     */
    boolean submit(Task task);

    /** 显式提交到指定队列 */
    boolean submit(String queueName, Task task);

    /**
     * 消费者调用：按 dispatch policy 拉取所有队列的可执行 task。
     *
     * @param totalPermit 本轮总拉取额度上限
     * @return 拉取到的 task 列表（可能小于 totalPermit，因各队列可能已空）
     */
    List<Task> pollAll(int totalPermit);

    /** 所有活跃队列 */
    Collection<DynamicQueue> queues();

    /** 全局共享执行线程池 */
    ExecutorService workerPool();
}
