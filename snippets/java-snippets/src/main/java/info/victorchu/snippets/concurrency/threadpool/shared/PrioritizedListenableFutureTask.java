package info.victorchu.snippets.concurrency.threadpool.shared;

import com.google.common.util.concurrent.ForwardingListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * 自定义的 ListenableFutureTask，实现 Comparable 接口
 */
public class PrioritizedListenableFutureTask<V>
        extends ForwardingListenableFuture<V> implements Comparable<PrioritizedListenableFutureTask<?>>,
        RunnableFuture<V>,ResourceGroupManaged {

    private final ListenableFutureTask<V> delegate;
    @Getter
    private final String resourceGroupId;
    @Getter
    private final int priority;

    public PrioritizedListenableFutureTask(Callable<V> callable, String resourceGroupId, int priority) {
        this.delegate = (ListenableFutureTask.create(callable));
        this.resourceGroupId = resourceGroupId;
        this.priority = priority;
    }

    public PrioritizedListenableFutureTask(Runnable runnable, V result, String resourceGroupId, int priority) {
        this.delegate =(ListenableFutureTask.create(runnable, result));
        this.resourceGroupId = resourceGroupId;
        this.priority = priority;
    }

    @Override
    public int compareTo(PrioritizedListenableFutureTask<?> other) {
        // 优先级高的排在前面
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PrioritizedListenableFutureTask<?> that = (PrioritizedListenableFutureTask<?>) obj;
        return priority == that.priority && Objects.equals(resourceGroupId, that.resourceGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceGroupId, priority);
    }

    @Override
    protected ListenableFuture<? extends V> delegate() {
        return delegate;
    }

    @Override
    public void run() {
        delegate.run();
    }
}
