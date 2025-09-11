package info.victorchu.snippets.concurrency.threadpool.priority;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPool
        extends ThreadPoolExecutor
{
    public PriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
    {
        return new PriorityFutureTask<>(runnable, value);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
    {
        return new PriorityFutureTask<T>(callable);
    }

    public Future<?> submit(Runnable task)
    {
        if (task == null) {
            throw new NullPointerException();
        }
        if(task instanceof PriorityRunnableTask){
            return super.submit(task);
        }
        throw new UnsupportedOperationException();
    }

    public <T> Future<T> submit(Runnable task, T result)
    {
        if (task == null) {
            throw new NullPointerException();
        }
        if(task instanceof PriorityRunnableTask){
            return super.submit(task,result);
        }
        throw new UnsupportedOperationException();
    }

    public <T> Future<T> submit(Callable<T> task)
    {
        if (task == null) {
            throw new NullPointerException();
        }
        if(task instanceof PriorityCallableTask){
            return super.submit(task);
        }
        throw new UnsupportedOperationException();
    }
}
