package info.victorchu.snippets.concurrency.threadpool.priority;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityFutureTask<T>
        extends FutureTask<T>
        implements Comparable<PriorityFutureTask<T>>
{

    private final Prioritized task;

    public PriorityFutureTask(Runnable task, T value)
    {
        super(task, value);
        this.task = (Prioritized) task;
    }

    public PriorityFutureTask(Callable<T> task)
    {
        super(task);
        this.task = (Prioritized) task;
    }

    @Override
    public int compareTo(PriorityFutureTask that)
    {
        return Integer.compare(this.task.getPriority(), that.task.getPriority());
    }
}
