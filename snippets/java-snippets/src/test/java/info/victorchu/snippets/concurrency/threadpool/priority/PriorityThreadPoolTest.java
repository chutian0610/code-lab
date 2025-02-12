package info.victorchu.snippets.concurrency.threadpool.priority;

import info.victorchu.snippets.compile.pratt.Lexer;
import info.victorchu.snippets.compile.pratt.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class PriorityThreadPoolTest
{
    static class SimpleRunnable
            implements Runnable
    {

        private final String name;

        public SimpleRunnable(String name)
        {
            this.name = name;
        }

        @Override
        public void run()
        {
            try {
                System.out.println(this.name + " triggered successfully");
                Thread.sleep(2000);
                System.out.println(this.name + " completed successfully");
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(interruptedException);
            }
        }
    }

    @Test
    void testParser01()
            throws InterruptedException
    {
        ThreadPoolExecutor tpe = new PriorityThreadPool(1, // core pool size
                1, // maximum pool size
                1000,  // keep alive time
                TimeUnit.SECONDS,  // time unit
                new PriorityBlockingQueue<>() // worker queue
        );

        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T5"), 5));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T4"), 4));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T3"), 3));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T2"), 2));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T1"), 1));
        Thread.currentThread().sleep(50000);
    }

    @Test
    void testParser02()
            throws InterruptedException
    {
        ThreadPoolExecutor tpe = new PriorityThreadPool(1, // core pool size
                1, // maximum pool size
                1000,  // keep alive time
                TimeUnit.SECONDS,  // time unit
                new BoundedPriorityBlockingQueue<>(2) // worker queue
        );

        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T5"), 5));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T4"), 4));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T3"), 3));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T2"), 2));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T1"), 1));
        Thread.currentThread().sleep(50000);
    }

    @Test
    void testParser03()
            throws InterruptedException
    {
        ThreadPoolExecutor tpe = new PriorityThreadPool(1, // core pool size
                3, // maximum pool size
                1000,  // keep alive time
                TimeUnit.SECONDS,  // time unit
                new BoundedPriorityBlockingQueue<>(2) // worker queue
        );

        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T5"), 5));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T4"), 4));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T3"), 3));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T2"), 2));
        tpe.submit(new PriorityRunnableTask(new SimpleRunnable("T1"), 1));
        Thread.currentThread().sleep(50000);
    }
}