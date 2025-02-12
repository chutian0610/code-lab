package info.victorchu.snippets.concurrency.threadpool.eager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskQueue
        extends LinkedBlockingQueue<Runnable>
{
    private volatile ThreadPoolExecutor executor;

    public void setExecutor(ThreadPoolExecutor executor)
    {
        this.executor = executor;
    }

    @Override
    public boolean offer(Runnable runnable)
    {
        if (executor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }

        int currentPoolThreadSize = executor.getPoolSize();
        // have free worker. put task into queue to let the worker deal with task.
        if (executor.getActiveCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }

        // return false to let executor create new worker.
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }

        // currentPoolThreadSize >= max
        return super.offer(runnable);
    }

    /**
     * Forcefully enqueue the rejected task.
     *
     * @param runnable task
     * @return offer success or not
     * @throws RejectedExecutionException if executor is terminated.
     */
    public boolean forceOffer(Runnable runnable, long timeout, TimeUnit unit)
            throws InterruptedException
    {
        if (executor == null || executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        return super.offer(runnable, timeout, unit);
    }
}
