package info.victorchu.snippets.concurrency.threadpool.shared;

import com.google.common.util.concurrent.SettableFuture;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@ToString
public class ResourceManagedTask<V> implements ResourceGroupManaged, Comparable<ResourceManagedTask<?>> {

    private final Callable<V> delegate;
    @Getter
    private final String resourceGroupId;
    @Getter
    private final int priority;
    @Getter
    private final SettableFuture<V> result = SettableFuture.create();

    public ResourceManagedTask(Callable<V> callable, String resourceGroupId, int priority) {
        this.delegate = callable;
        this.resourceGroupId = resourceGroupId;
        this.priority = priority;
    }

    public ResourceManagedTask(Runnable runnable, V result, String resourceGroupId, int priority) {
        this.delegate = Executors.callable(runnable,result);
        this.resourceGroupId = resourceGroupId;
        this.priority = priority;
    }

    public ResourceManagedTask(Runnable runnable, String resourceGroupId, int priority) {
        this.delegate = Executors.callable(runnable,null);
        this.resourceGroupId = resourceGroupId;
        this.priority = priority;
    }

    @Override
    public int compareTo(ResourceManagedTask<?> other) {
        // 优先级高的排在前面
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public String getResourceGroupId() {
        return resourceGroupId;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public Callable<V> getDelegate(){
        return delegate;
    }
}
