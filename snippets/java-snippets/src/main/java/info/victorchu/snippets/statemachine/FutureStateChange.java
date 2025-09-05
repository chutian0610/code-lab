package info.victorchu.snippets.statemachine;

import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.Objects.requireNonNull;

@ThreadSafe
public class FutureStateChange<T>
{
    // Use a separate future for each listener so canceled listeners can be removed
    @GuardedBy("listeners")
    private final Set<SettableFuture<T>> listeners = new HashSet<>();

    public ListenableFuture<T> createNewListener()
    {
        SettableFuture<T> listener = SettableFuture.create();
        synchronized (listeners) {
            listeners.add(listener);
        }

        // remove the listener when the future completes
        listener.addListener(
                () -> {
                    synchronized (listeners) {
                        listeners.remove(listener);
                    }
                },
                directExecutor());

        return listener;
    }

    public void complete(T newState)
    {
        fireStateChange(newState, directExecutor());
    }

    public void complete(T newState, Executor executor)
    {
        fireStateChange(newState, executor);
    }

    private void fireStateChange(T newState, Executor executor)
    {
        requireNonNull(executor, "executor is null");
        Set<SettableFuture<T>> futures;
        synchronized (listeners) {
            futures = ImmutableSet.copyOf(listeners);
            listeners.clear();
        }

        for (SettableFuture<T> future : futures) {
            executor.execute(() -> future.set(newState));
        }
    }
}
