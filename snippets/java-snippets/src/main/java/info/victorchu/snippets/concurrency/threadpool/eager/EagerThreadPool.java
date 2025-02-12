package info.victorchu.snippets.concurrency.threadpool.eager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 优先增长线程数的线程池
 */
public class EagerThreadPool
        extends ThreadPoolExecutor
{
    public EagerThreadPool(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command)
    {
        if (command == null) {
            throw new NullPointerException();
        }
        executeInternal(command, 0, TimeUnit.MILLISECONDS);
    }

    public void executeInternal(Runnable command, long timeout, TimeUnit unit)
    {
        try {
            super.execute(command);
        }
        catch (RejectedExecutionException rx) {
            if (getQueue() instanceof TaskQueue) {
                // If the Executor is close to maximum pool size, concurrent
                // calls to execute() may result in some tasks being rejected rather than queued.
                // If this happens, add them to the queue.
                final TaskQueue queue = (TaskQueue) getQueue();
                try {
                    if (!queue.forceOffer(command, timeout, unit)) {
                        throw new RejectedExecutionException("Queue capacity is full.", rx);
                    }
                }
                catch (InterruptedException x) {
                    throw new RejectedExecutionException(x);
                }
            }
            else {
                throw rx;
            }
        }
    }
}

