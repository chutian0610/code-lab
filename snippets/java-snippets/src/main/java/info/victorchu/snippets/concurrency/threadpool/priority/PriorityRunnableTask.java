package info.victorchu.snippets.concurrency.threadpool.priority;

public class PriorityRunnableTask
        implements Runnable, Prioritized
{
    private Runnable runnable;
    private final int priority;

    PriorityRunnableTask(Runnable runnable, int priority)
    {
        this.runnable = runnable;
        this.priority = priority;
    }

    @Override
    public int getPriority()
    {
        return priority;
    }

    @Override
    public void run()
    {
        runnable.run();
    }
}