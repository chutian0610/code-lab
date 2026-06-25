package info.victorchu.snippets.queue.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.api.TaskScheduler;

/**
 * {@link DynamicQueue} 的默认实现。
 *
 * <h3>线程模型</h3>
 * <ul>
 *   <li>一把 {@link ReentrantLock} 保护队列内状态（waiting、closed、capacity/concurrency 写）</li>
 *   <li>{@code scheduler} 字段用 volatile，{@link #config()} 可无锁读</li>
 *   <li>{@code capacity} / {@code concurrency} / {@code closed} 用 volatile</li>
 *   <li>{@code runningSize} 用 {@link AtomicInteger}，无锁读</li>
 * </ul>
 *
 * <h3>与 TaskScheduler 的协作</h3>
 * 所有 {@code TaskScheduler} 方法都在本队列的 lock 内调用，故 scheduler 自身可非线程安全
 * （每个队列独立实例）。
 */
public class DefaultDynamicQueue implements DynamicQueue {

    private final String name;

    /** 保护 closed、capacity/concurrency 写、以及所有 scheduler 方法的调用 */
    private final ReentrantLock lock = new ReentrantLock();

    /** 当前生效的调度策略（volatile：updateScheduler 持锁换引用，config() 可无锁读） */
    private volatile TaskScheduler scheduler;

    /** 最大排队数（volatile：写时仍持锁，外部读可无锁） */
    private volatile int capacity;

    /** 最大并发执行数 */
    private volatile int concurrency;

    /** 正在执行的 task 数 */
    private final AtomicInteger runningSize = new AtomicInteger(0);

    /** 是否已关闭 */
    private volatile boolean closed = false;

    public DefaultDynamicQueue(QueueConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.name = config.name();
        this.capacity = config.capacity();
        this.concurrency = config.concurrency();
        this.scheduler = config.scheduler();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public QueueConfig config() {
        // scheduler 字段是 volatile，无锁读即可获得一致的引用
        return QueueConfig.of(name, capacity, concurrency, scheduler);
    }

    @Override
    public boolean tryEnqueue(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        lock.lock();
        try {
            // 已关闭或容量已满则拒绝
            if (closed || scheduler.size() >= capacity) {
                return false;
            }
            return scheduler.offer(task);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Task> pollReady(int permit) {
        if (permit <= 0) {
            return Collections.emptyList();
        }
        lock.lock();
        try {
            if (closed) {
                return Collections.emptyList();
            }
            int available = concurrency - runningSize.get();
            if (available <= 0) {
                return Collections.emptyList();
            }
            int n = Math.min(permit, available);
            if (n == 0) {
                return Collections.emptyList();
            }
            List<Task> out = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                Task t = scheduler.poll();
                if (t == null) {
                    break;  // scheduler 提前耗尽
                }
                runningSize.incrementAndGet();
                out.add(t);
            }
            return out;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void markFinished(Task task) {
        // 与 pollReady 的 incrementAndGet 对称：在 lock 内递减，避免 CAS 自旋的复杂度。
        // markFinished 在 worker 线程高频调用，但临界区只一次 getAndDecrement，开销极低。
        lock.lock();
        try {
            int cur = runningSize.get();
            if (cur > 0) {
                runningSize.decrementAndGet();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateCapacity(int newCapacity) {
        if (newCapacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0, got " + newCapacity);
        }
        // 必须在 lock 内写：tryEnqueue 的 "size >= capacity" 与 offer 是 check-then-act
        lock.lock();
        try {
            this.capacity = newCapacity;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateConcurrency(int newConcurrency) {
        if (newConcurrency <= 0) {
            throw new IllegalArgumentException("concurrency must be > 0, got " + newConcurrency);
        }
        // 同 updateCapacity：pollReady 的 "concurrency - runningSize" check-then-act 需要原子
        lock.lock();
        try {
            this.concurrency = newConcurrency;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateScheduler(TaskScheduler newScheduler) {
        if (newScheduler == null) {
            throw new IllegalArgumentException("scheduler must not be null");
        }
        lock.lock();
        try {
            // 迁移：把旧 scheduler 中所有等待 task 投递到新 scheduler
            for (Task t : this.scheduler.snapshot()) {
                boolean accepted = newScheduler.offer(t);
                if (!accepted) {
                    // 新存储拒绝（理论上容量不受限，仅极端情况）—— 直接丢弃
                }
            }
            this.scheduler.clear();
            this.scheduler = newScheduler;  // volatile 写
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        lock.lock();
        try {
            this.closed = true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int waitingSize() {
        lock.lock();
        try {
            return scheduler.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int runningSize() {
        return runningSize.get();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
