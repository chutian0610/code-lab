package info.victorchu.snippets.tasks.simple;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.Objects.requireNonNull;

public class MoreFutures {
    /**
     * Invokes the callback if the future fails. Note, this uses the direct
     * executor, so the callback should not be resource intensive.
     */
    public static <T> void addExceptionCallback(ListenableFuture<T> future, Consumer<Throwable> exceptionCallback)
    {
        addExceptionCallback(future, exceptionCallback, directExecutor());
    }

    /**
     * Invokes the callback, using the specified executor, if the future fails.
     */
    public static <T> void addExceptionCallback(ListenableFuture<T> future, Consumer<Throwable> exceptionCallback, Executor executor)
    {
        requireNonNull(future, "future is null");
        requireNonNull(exceptionCallback, "exceptionCallback is null");

        FutureCallback<T> callback = new FutureCallback<T>()
        {
            @Override
            public void onSuccess(@Nullable T result) {}

            @Override
            public void onFailure(Throwable t)
            {
                exceptionCallback.accept(t);
            }
        };
        Futures.addCallback(future, callback, executor);
    }

    /**
     * Invokes the callback if the future fails. Note, this uses the direct
     * executor, so the callback should not be resource intensive.
     */
    public static <T> void addExceptionCallback(ListenableFuture<T> future, Runnable exceptionCallback)
    {
        addExceptionCallback(future, exceptionCallback, directExecutor());
    }

    /**
     * Invokes the callback, using the specified executor, if the future fails.
     */
    public static <T> void addExceptionCallback(ListenableFuture<T> future, Runnable exceptionCallback, Executor executor)
    {
        requireNonNull(exceptionCallback, "exceptionCallback is null");

        addExceptionCallback(future, t -> exceptionCallback.run(), executor);
    }
    /**
     * Invokes the callback if the future completes successfully. Note, this uses the direct
     * executor, so the callback should not be resource intensive.
     */
    public static <T> void addSuccessCallback(ListenableFuture<T> future, Consumer<T> successCallback)
    {
        addSuccessCallback(future, successCallback, directExecutor());
    }

    /**
     * Invokes the callback, using the specified executor, if the future completes successfully.
     */
    public static <T> void addSuccessCallback(ListenableFuture<T> future, Consumer<T> successCallback, Executor executor)
    {
        requireNonNull(future, "future is null");
        requireNonNull(successCallback, "successCallback is null");

        FutureCallback<T> callback = new FutureCallback<T>()
        {
            @Override
            public void onSuccess(@Nullable T result)
            {
                successCallback.accept(result);
            }

            @Override
            public void onFailure(Throwable t) {}
        };
        Futures.addCallback(future, callback, executor);
    }

    /**
     * Invokes the callback if the future completes successfully. Note, this uses the direct
     * executor, so the callback should not be resource intensive.
     */
    public static <T> void addSuccessCallback(ListenableFuture<T> future, Runnable successCallback)
    {
        addSuccessCallback(future, successCallback, directExecutor());
    }

    /**
     * Invokes the callback, using the specified executor, if the future completes successfully.
     */
    public static <T> void addSuccessCallback(ListenableFuture<T> future, Runnable successCallback, Executor executor)
    {
        requireNonNull(successCallback, "successCallback is null");

        addSuccessCallback(future, t -> successCallback.run(), executor);
    }

    public static <T> void addCallback(ListenableFuture<T> future, FutureCallback<T> futureCallback, Executor executor)
    {
        requireNonNull(future, "future is null");
        requireNonNull(futureCallback, "futureCallback is null");
        Futures.addCallback(future, futureCallback, executor);
    }

    public static <T> void addCallback(ListenableFuture<T> future, FutureCallback<T> futureCallback)
    {
        addCallback(future, futureCallback, directExecutor());
    }

    public static void safeExecute(Runnable command,Executor executor)
    {
        try {
            executor.execute(command);
        }
        catch (RejectedExecutionException e) {
            if ((executor instanceof ExecutorService) && ((ExecutorService) executor).isShutdown()) {
                throw new RuntimeException("ERROR_SERVER_SHUTTING_DOWN", e);
            }
            throw e;
        }
    }
}
