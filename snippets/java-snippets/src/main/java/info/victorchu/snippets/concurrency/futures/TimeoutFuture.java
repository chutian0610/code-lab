package info.victorchu.snippets.concurrency.futures;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.j2objc.annotations.RetainedLocalRef;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @see com.google.common.util.concurrent.TimeoutFuture
 * @param <V>
 */
public class TimeoutFuture<V extends @Nullable Object> extends AbstractFuture<V> {

    public static <V extends @Nullable Object> ListenableFuture<V> withTimeout(
            ListenableFuture<V> delegate,
            long time,
            TimeUnit unit,
            ScheduledExecutorService scheduledExecutor) {
        if (delegate.isDone()) {
            return delegate;
        }
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor,true);
    }
    public static <V extends @Nullable Object> ListenableFuture<V> withTimeLimit(
            ListenableFuture<V> delegate,
            long time,
            TimeUnit unit,
            ScheduledExecutorService scheduledExecutor) {
        if (delegate.isDone()) {
            return delegate;
        }
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor,false);
    }
    static <V extends @Nullable Object> ListenableFuture<V> create(
            ListenableFuture<V> delegate,
            long time,
            TimeUnit unit,
            ScheduledExecutorService scheduledExecutor,
            boolean cancelDelegate) {
        TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
        TimeoutFuture.Fire<V> fire = new TimeoutFuture.Fire<>(result,cancelDelegate);
        result.timer = scheduledExecutor.schedule(fire, time, unit);
        delegate.addListener(fire, directExecutor());
        return result;
    }

    private ListenableFuture<V> delegateRef;
    private ScheduledFuture<?> timer;

    private TimeoutFuture(ListenableFuture<V> delegate) {
        this.delegateRef = Preconditions.checkNotNull(delegate);
    }
    static final class Fire<V extends @Nullable Object> implements Runnable {
        TimeoutFuture<V> timeoutFutureRef;
        boolean cancelDelegate = false;

        Fire(TimeoutFuture<V> timeoutFuture, boolean cancelDelegate) {
            this.timeoutFutureRef = timeoutFuture;
            this.cancelDelegate = cancelDelegate;
        }

        @Override
        @SuppressWarnings("Interruption")
        public void run() {
            // If either of these reads return null then we must be after a successful cancel or another
            // call to this method.
            TimeoutFuture<V> timeoutFuture = timeoutFutureRef;
            if (timeoutFuture == null) {
                return;
            }
            ListenableFuture<V> delegate = timeoutFuture.delegateRef;
            if (delegate == null) {
                return;
            }
            timeoutFutureRef = null;
            if (delegate.isDone()) {
                timeoutFuture.setFuture(delegate);
            } else {
                try {
                    @RetainedLocalRef ScheduledFuture<?> timer = timeoutFuture.timer;
                    timeoutFuture.timer = null; // Don't include already elapsed delay in delegate.toString()
                    String message = "Timed out";
                    // This try-finally block ensures that we complete the timeout future, even if attempting
                    // to produce the message throws (probably StackOverflowError from delegate.toString())
                    try {
                        if (timer != null) {
                            long overDelayMs = Math.abs(timer.getDelay(MILLISECONDS));
                            if (overDelayMs > 10) { // Not all timing drift is worth reporting
                                message += " (timeout delayed by " + overDelayMs + " ms after scheduled time)";
                            }
                        }
                        message += ": " + delegate;
                    } finally {
                        timeoutFuture.setException(new TimeoutFuture.TimeoutFutureException(message));
                    }
                } finally {
                    if(cancelDelegate) {
                        delegate.cancel(true);
                    }
                }
            }
        }
    }
    public static final class TimeoutFutureException extends TimeoutException {
        private TimeoutFutureException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            setStackTrace(new StackTraceElement[0]);
            return this; // no stack trace, wouldn't be useful anyway
        }
    }
}