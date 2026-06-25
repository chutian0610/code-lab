package info.victorchu.snippets.queue.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import info.victorchu.snippets.queue.api.DispatchPlan;
import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.QueueDispatchPolicy;
import info.victorchu.snippets.queue.api.QueueManager;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.policy.FifoDispatchPolicy;

/**
 * {@link QueueManager} 的默认实现。
 *
 * <h3>关键设计</h3>
 * <ul>
 *   <li>队列存储用 {@link ConcurrentHashMap}，创建/删除通过 {@code putIfAbsent} / {@code remove} 原子操作</li>
 *   <li>持有全局共享的 {@link ExecutorService} workerPool，所有队列复用</li>
 *   <li>持有 {@link QueueDispatchPolicy}，{@link #pollAll} 委派给它计算拉取计划</li>
 *   <li>{@link #removeQueue} 异步排空：复用单一 {@link ScheduledExecutorService} 调度</li>
 * </ul>
 */
public class DefaultQueueManager implements QueueManager {

    private final ConcurrentHashMap<String, DefaultDynamicQueue> queues = new ConcurrentHashMap<>();
    private final ExecutorService workerPool;
    private final QueueDispatchPolicy dispatchPolicy;

    /**
     * 异步排空专用调度器：单一线程 + daemon，复用于所有 removeQueue 操作，
     * 避免每队列 new 守护线程。
     */
    private final ScheduledExecutorService drainExecutor;

    public DefaultQueueManager(ExecutorService workerPool) {
        this(workerPool, new FifoDispatchPolicy());
    }

    public DefaultQueueManager(ExecutorService workerPool, QueueDispatchPolicy dispatchPolicy) {
        this.workerPool = Objects.requireNonNull(workerPool, "workerPool");
        this.dispatchPolicy = Objects.requireNonNull(dispatchPolicy, "dispatchPolicy");
        this.drainExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "queue-drainer");
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public DynamicQueue createQueue(QueueConfig config) {
        Objects.requireNonNull(config, "config");
        DefaultDynamicQueue q = new DefaultDynamicQueue(config);
        DefaultDynamicQueue prev = queues.putIfAbsent(config.name(), q);
        return prev == null ? q : null;
    }

    @Override
    public boolean removeQueue(String name) {
        DefaultDynamicQueue q = queues.get(name);
        if (q == null) {
            return false;
        }
        q.close();
        // 排空：周期性检查 runningSize，== 0 时从 map 移除。
        // 用 AtomicBoolean 防止多次 schedule 重复排空（罕见但防御性）。
        AtomicBoolean done = new AtomicBoolean(false);
        drainExecutor.scheduleWithFixedDelay(() -> {
            if (q.runningSize() > 0) {
                return;
            }
            // 双重检查：可能上一轮已经成功
            if (done.compareAndSet(false, true)) {
                queues.remove(name, q);
            }
        }, 50, 50, TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public DynamicQueue getQueue(String name) {
        return queues.get(name);
    }

    @Override
    public boolean exists(String name) {
        return queues.containsKey(name);
    }

    @Override
    public void updateConfig(String name, QueueConfig newConfig) {
        Objects.requireNonNull(newConfig, "newConfig");
        DefaultDynamicQueue q = queues.get(name);
        if (q == null) {
            return;
        }
        if (!newConfig.name().equals(name)) {
            throw new IllegalArgumentException(
                    "name mismatch: key=" + name + ", newConfig.name=" + newConfig.name());
        }
        q.updateCapacity(newConfig.capacity());
        q.updateConcurrency(newConfig.concurrency());
        q.updateScheduler(newConfig.scheduler());
    }

    @Override
    public boolean submit(Task task) {
        if (task == null) {
            return false;
        }
        return submit(task.targetQueue(), task);
    }

    @Override
    public boolean submit(String queueName, Task task) {
        if (queueName == null || task == null) {
            return false;
        }
        DynamicQueue q = queues.get(queueName);
        if (q == null) {
            return false;
        }
        return q.tryEnqueue(task);
    }

    @Override
    public List<Task> pollAll(int totalPermit) {
        if (totalPermit <= 0 || queues.isEmpty()) {
            return Collections.emptyList();
        }
        DispatchPlan plan = dispatchPolicy.plan(queues.values(), totalPermit);
        List<Task> out = new ArrayList<>(totalPermit);
        for (String name : plan.orderedNames()) {
            Integer permitObj = plan.permits().get(name);
            if (permitObj == null) {
                continue;
            }
            int n = permitObj;
            if (n <= 0) {
                continue;
            }
            DynamicQueue q = queues.get(name);
            if (q == null) {
                continue;
            }
            // 一次锁内批量取 N 个，比循环 pollReady(N) 少 N-1 次锁
            List<Task> tasks = q.pollReady(n);
            if (!tasks.isEmpty()) {
                out.addAll(tasks);
            }
        }
        return out;
    }

    @Override
    public Collection<DynamicQueue> queues() {
        return Collections.unmodifiableCollection(queues.values());
    }

    @Override
    public ExecutorService workerPool() {
        return workerPool;
    }

    /**
     * 关闭异步排空调度器。一般无需调用；仅在测试或显式清理时使用。
     */
    public void shutdownDrainer() {
        drainExecutor.shutdownNow();
    }
}
