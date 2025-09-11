package info.victorchu.snippets.concurrency.threadpool.shared;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import info.victorchu.snippets.concurrency.util.Threads;
import info.victorchu.snippets.tasks.simple.MoreFutures;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;

public class ResourceSharedPool {

    /**
     * 支持优先级的 Callable
     */
    public interface PrioritizedCallable<V> extends Callable<V> ,ResourceGroupManaged {
        String getResourceGroupId();
        int getPriority();
    }

    /**
     * 支持优先级的 Runnable
     */
    public interface PrioritizedRunnable extends Runnable,ResourceGroupManaged {
        String getResourceGroupId();
        int getPriority();
    }

    private final ScheduledExecutorService dispatcher;
    private final MultiQueueDualSemaphore<ResourceManagedTask> taskQueue;
    private final ListeningExecutorService worker;
    private final ThreadPoolExecutor workerThreadPoolExecutor;

    public ResourceSharedPool(
            int capacity, ResourceGroupManager resourceGroupManager, ThreadFactory threadFactory) {
        this.dispatcher = Executors.newScheduledThreadPool(2,Threads.daemonThreadsNamed("scheduler-%s"));
        this.taskQueue = new MultiQueueDualSemaphore<>(capacity, resourceGroupManager);
        this.workerThreadPoolExecutor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
        this.worker = listeningDecorator(Executors.newCachedThreadPool());
        start();
    }

    public void start(){
        dispatcher.scheduleWithFixedDelay(()->{
           ResourceManagedTask<Object> task =null;
           while ((task = taskQueue.poll()) != null){
               final  ResourceManagedTask<Object> rt = task;
               ListenableFuture future = worker.submit(task.getDelegate());
               MoreFutures.addCallback(future, new FutureCallback<Object>() {
                   @Override
                   public void onSuccess(@Nullable Object result) {
                       rt.getResult().set(result);
                       taskQueue.taskCompleted(rt.getResourceGroupId());
                   }

                   @Override
                   public void onFailure(Throwable t) {
                       rt.getResult().setException(t);
                       taskQueue.taskCompleted(rt.getResourceGroupId());
                   }
               });
               MoreFutures.addExceptionCallback(rt.getResult(), (Throwable t)->{
                   if (t instanceof java.util.concurrent.CancellationException) {
                       future.cancel(true);
                   }
               });
           }
        },1,1,TimeUnit.MILLISECONDS);
    }

    public void close() throws InterruptedException {
        dispatcher.shutdown();
        dispatcher.awaitTermination(30,TimeUnit.SECONDS);
        worker.shutdown();
        worker.awaitTermination(30, TimeUnit.SECONDS);
    }

    private <T> ResourceManagedTask<T> wrapAsCallable(Callable<T> callable) {
        if (callable instanceof PrioritizedCallable) {
            PrioritizedCallable<T> prioritizedCallable = (PrioritizedCallable<T>) callable;
            return new ResourceManagedTask<>(
                    prioritizedCallable,
                    prioritizedCallable.getResourceGroupId(),
                    prioritizedCallable.getPriority()
            );
        }
        throw new UnsupportedOperationException();
    }

    protected <T> ResourceManagedTask<T> wrapAsCallable(Runnable runnable, T value) {
        if (runnable instanceof PrioritizedRunnable) {
            PrioritizedRunnable prioritizedRunnable = (PrioritizedRunnable) runnable;
            return new ResourceManagedTask<>(
                    Executors.callable(runnable, value),
                    prioritizedRunnable.getResourceGroupId(),
                    prioritizedRunnable.getPriority()
            );
        }
        throw new UnsupportedOperationException();
    }

    public ListenableFuture<?> submit(Runnable task) {
        ResourceManagedTask<?> rt = wrapAsCallable(task,null);
        submitTask(rt);
        return rt.getResult();
    }

    public <T> ListenableFuture<T> submit(Runnable task, @Nullable T result) {
        ResourceManagedTask<T> rt = wrapAsCallable(task,result);
        submitTask(rt);
        return rt.getResult();
    }

    public <T> ListenableFuture<T> submit(Callable<T> task) {
        ResourceManagedTask<T> rt = wrapAsCallable(task);
        submitTask(rt);
        return rt.getResult();
    }
    private <T> void submitTask(ResourceManagedTask<T> rt){
        ListenableFuture<?> submit =  worker.submit(()->{
            taskQueue.offer(rt);
        });
        MoreFutures.addExceptionCallback(submit,(Throwable t)->{
            rt.getResult().setException(t);
        });
        MoreFutures.addExceptionCallback(rt.getResult(), (Throwable t)->{
            if (t instanceof java.util.concurrent.CancellationException) {
                submit.cancel(true);
            }
        });
    }



    private static String getTaskType(int index) {
        String[] types = {
                "API_CALL",
                "FILE_PROCESS",
                "DB_QUERY"
        };
        return types[index % types.length];
    }

    public static void main(String[] args) throws Exception {
        ResourceGroupManager resourceGroupManager = new ResourceGroupManager(Lists.newArrayList(
                new ResourceGroup("API_CALL",15,15),
                new ResourceGroup("FILE_PROCESS",15,15),
                new ResourceGroup("DB_QUERY",3,1)
        ));
        // 创建自定义执行器
        ResourceSharedPool executor =
                new ResourceSharedPool(50, resourceGroupManager, Threads.daemonThreadsNamed("runner-%s"));

        // 提交任务
        for (int i = 0; i < 5; i++) {
            String taskType = getTaskType(i);
            int priority = (int) (Math.random() * 10);
            int taskId = i;

            // 创建优先级的 Callable
            ResourceSharedPool.PrioritizedCallable<String> task =
                    new ResourceSharedPool.PrioritizedCallable<String>() {
                        @Override
                        public String call() throws Exception {
                            System.out.println(System.currentTimeMillis()+ " 执行任务 " + taskId + " [" + taskType +
                                    ", prio:" + priority + "]");
                            Thread.sleep(6* 1000);
                            return "任务 " + taskId + " 结果";
                        }

                        @Override
                        public String getResourceGroupId() {
                            return taskType;
                        }

                        @Override
                        public int getPriority() {
                            return priority;
                        }
                    };
            System.out.println(System.currentTimeMillis()+ " 提交任务 " + taskId + " [" + taskType +
                    ", prio:" + priority + "]");
            ListenableFuture<String> future = executor.submit(task);

            // 添加回调
            Futures.addCallback(future, new FutureCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    System.out.println(System.currentTimeMillis()+"成功: " + result);
                }

                @Override
                public void onFailure(Throwable t) {
                    System.err.println(System.currentTimeMillis()+"失败: " + t.getMessage());
                }
            }, MoreExecutors.directExecutor());
        }
        // 等待任务完成
        Thread.sleep(120* 1000);
        executor.close();
    }
}