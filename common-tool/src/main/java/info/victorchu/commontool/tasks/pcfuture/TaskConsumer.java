package info.victorchu.commontool.tasks.pcfuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.concurrent.*;

/**
 * task 消费者
 * @author victorchu
 * @date 2022/8/8 20:20
 */
@Slf4j
public class TaskConsumer {

    /**
     * task 队列
     */
    private final TaskLimitBlockQueue queue;

    /**
     * task 执行池
     */
    private final ExecutorService executorService =
            new ThreadPoolExecutor(10,10, 0L, TimeUnit.MILLISECONDS,
                    new SynchronousQueue<Runnable>(),
                    new CustomizableThreadFactory("TaskExecutor-"),
                    new CallerBlocksPolicy(0)
            );
    /**
     * task consumer loop 线程
     */
    private final ExecutorService selfExecutor =
            new ThreadPoolExecutor(1,1, 0L,TimeUnit.MILLISECONDS,
                    new SynchronousQueue<Runnable>(),
                    new CustomizableThreadFactory("TaskConsumer-"),
                    new ThreadPoolExecutor.AbortPolicy()
            );

    public TaskConsumer(TaskLimitBlockQueue queue) {
        this.queue = queue;
    }

    public void start(){
        selfExecutor.submit(()->{
            while(true){
                try {
                    log.debug("availablePermits:{}",queue.availablePermits());
                    Task task=queue.take();
                    submitTask(task);
                }catch (Exception e){
                    if(log.isDebugEnabled()) {
                        log.debug("task consumer error:", e);
                    }
                }
            }
        });
    }

    public Task submitTask(Task task){
        if(task.isCancelled()){
            return task;
        }
        log.info("submit task {}",task.getName());
        // 在callback中处理任务结果
        ListenableFutureTask<Object> listenableFutureTask = new ListenableFutureTask<Object>(() -> execute(task));
        listenableFutureTask.addCallback(new SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                log.debug("task {} success",task.getName());
                task.setResult(result);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                log.debug("task {} error",task.getName());
                task.setException(ex);
            }
        });
        Future<?> future= executorService.submit(listenableFutureTask);
        task.onTake(future);
        return task;
    }

    public Object execute(Task task) throws InterruptedException {
        // here return task result
        return new Object(){};
    }
}
