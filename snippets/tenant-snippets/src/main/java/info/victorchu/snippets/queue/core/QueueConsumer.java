package info.victorchu.snippets.queue.core;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueManager;
import info.victorchu.snippets.queue.api.Task;

/**
 * 统一消费者。
 *
 * <p>由单线程 {@link ScheduledExecutorService} 周期性 tick，</p>
 * <ol>
 *   <li>循环调 {@link QueueManager#pollAll(int)} 拉取所有队列的可执行 task,
 *       每次最多 {@code batchPerRound} 个,直到本次拿不到为止</li>
 *   <li>把 task 提交到 Manager 持有的全局 worker 线程池执行</li>
 *   <li>执行完毕（成功或异常）回调 {@link DynamicQueue#markFinished(Task)} 归还并发额度</li>
 * </ol>
 *
 * <p><b>线程模型</b>：单调度线程 + N 个 worker 线程。tick 本身不阻塞调度线程,
 * 任务执行由 worker 池异步完成;循环内每次拿到的 task 立即丢给 worker 池,
 * 不会等 worker markFinished,因此单次 tick 实际能"耗尽"当前可派 task。</p>
 *
 * <p><b>调度策略</b>：使用 {@link java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay}
 * 而非 {@code scheduleAtFixedRate},确保上一次 tick 结束后再等
 * {@code pollIntervalMs} 才开始下一次,杜绝并发 tick。</p>
 */
public class QueueConsumer {

    private final QueueManager manager;
    private final long pollIntervalMs;
    private final int batchPerRound;
    private final ScheduledExecutorService scheduler;

    public QueueConsumer(QueueManager manager, long pollIntervalMs, int batchPerRound) {
        this.manager = Objects.requireNonNull(manager, "manager");
        if (pollIntervalMs <= 0) {
            throw new IllegalArgumentException("pollIntervalMs must be > 0");
        }
        if (batchPerRound <= 0) {
            throw new IllegalArgumentException("batchPerRound must be > 0");
        }
        this.pollIntervalMs = pollIntervalMs;
        this.batchPerRound = batchPerRound;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "queue-consumer");
            t.setDaemon(true);
            return t;
        });
    }

    /** 启动周期拉取 */
    public void start() {
        // 用 scheduleWithFixedDelay：上一轮 tick 结束后再等 pollIntervalMs 才发起下一轮，
        // 避免 tick 本身超过 pollIntervalMs 时多个 tick 在同一调度线程上重叠。
        // 由于调度器是单线程的，tick 自身不需要再加锁防御并发。
        scheduler.scheduleWithFixedDelay(
                this::tick, pollIntervalMs, pollIntervalMs, TimeUnit.MILLISECONDS);
    }

    /** 停止调度（已派发的 task 不等待完成） */
    public void stop() {
        scheduler.shutdownNow();
    }

    /**
     * 生产用 tick：循环调 {@link #tickOnce()} 直到拿不到 task 为止。
     *
     * <p>行为:循环调 {@link QueueManager#pollAll(int)} 拿 task 提交到 worker 池,
     * 直到本次拿不到 task 为止。这样能"耗尽"当前可派的 task(worker 异步
     * {@code markFinished} 会慢慢释放 concurrency 槽位,循环能拿到更多)。</p>
     *
     * <p>退出条件:某次 {@code tickOnce} 返回 false(说明所有队列都空了
     * 或所有 concurrency 槽位都被占满)。</p>
     *
     * <p><b>不会无限循环</b>:tick 仅把 task 提交到 worker 池(异步执行),
     * 主线程不被阻塞。槽位占满时 {@code pollAll} 拿 0 个,自然退出。</p>
     */
    public void tick() {
        try {
            while (tickOnce()) {
                // 拿到的 task 已丢给 worker 池,本轮继续看是否还能再拿
            }
        } catch (Throwable e) {
            // tick 整体失败不应影响后续 tick；生产环境应做日志
        }
    }

    /**
     * 单次 pollAll:调一次 {@link QueueManager#pollAll(int)} 拿最多
     * {@code batchPerRound} 个 task 提交到 worker 池。
     *
     * @return true 拿到了至少一个 task;false 没拿到(队列空 / 槽位占满)
     */
    public boolean tickOnce() {
        try {
            List<Task> tasks = manager.pollAll(batchPerRound);
            if (tasks.isEmpty()) {
                return false;
            }
            for (Task t : tasks) {
                manager.workerPool().execute(() -> runTask(t));
            }
            return true;
        } catch (Throwable e) {
            // 单次 pollAll 失败不应影响后续 tick；生产环境应做日志
            return false;
        }
    }

    /** 包装 task 执行与 markFinished 回调 */
    private void runTask(Task t) {
        try {
            t.execute();
        } catch (Throwable e) {
            // 任务执行失败被吞掉（snippet 阶段）
            // 生产环境应做日志 / 死信 / 告警
        } finally {
            // 队列可能已被移除，做空指针保护
            DynamicQueue q = manager.getQueue(t.targetQueue());
            if (q != null) {
                q.markFinished(t);
            }
        }
    }
}
