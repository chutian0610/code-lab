package info.victorchu.snippets.queue.api;

import java.util.Objects;

/**
 * 队列不可变配置快照。
 * <p>所有字段在创建时确定，运行时修改通过 {@link DynamicQueue#updateCapacity(int)}、
 * {@link DynamicQueue#updateConcurrency(int)}、{@link DynamicQueue#updateScheduler(TaskScheduler)}。</p>
 *
 * <p>手写实现以避免 lombok 注解处理器在 JDK 25 上的兼容性问题。</p>
 */
public final class QueueConfig {

    private final String name;
    private final int capacity;
    private final int concurrency;
    private final TaskScheduler scheduler;

    public QueueConfig(String name, int capacity, int concurrency, TaskScheduler scheduler) {
        this.name = Objects.requireNonNull(name, "name");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0, got " + capacity);
        }
        if (concurrency <= 0) {
            throw new IllegalArgumentException("concurrency must be > 0, got " + concurrency);
        }
        this.capacity = capacity;
        this.concurrency = concurrency;
    }

    /**
     * 工厂方法。
     */
    public static QueueConfig of(String name, int capacity, int concurrency, TaskScheduler scheduler) {
        return new QueueConfig(name, capacity, concurrency, scheduler);
    }

    public String name() {
        return name;
    }

    public int capacity() {
        return capacity;
    }

    public int concurrency() {
        return concurrency;
    }

    public TaskScheduler scheduler() {
        return scheduler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueueConfig)) {
            return false;
        }
        QueueConfig that = (QueueConfig) o;
        return capacity == that.capacity
                && concurrency == that.concurrency
                && name.equals(that.name)
                && scheduler.equals(that.scheduler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, capacity, concurrency, scheduler);
    }

    @Override
    public String toString() {
        return "QueueConfig{name=" + name
                + ", capacity=" + capacity
                + ", concurrency=" + concurrency
                + ", scheduler=" + scheduler.getClass().getSimpleName()
                + "}";
    }
}
