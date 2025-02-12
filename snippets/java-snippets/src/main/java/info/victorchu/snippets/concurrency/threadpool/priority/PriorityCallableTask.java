package info.victorchu.snippets.concurrency.threadpool.priority;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class PriorityCallableTask<T>
        implements Callable<T>, Prioritized
{
    private Callable<T> callable;
    private final int priority;

    PriorityCallableTask(Callable<T> callable, int priority)
    {
        this.callable = callable;
        this.priority = priority;
    }

    public int getPriority()
    {
        return priority;
    }

    @Override
    public T call()
            throws Exception
    {
        return callable.call();
    }
}