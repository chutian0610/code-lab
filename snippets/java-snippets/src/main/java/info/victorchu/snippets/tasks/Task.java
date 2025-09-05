package info.victorchu.snippets.tasks;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static info.victorchu.snippets.tasks.MoreFutures.safeExecute;
import static info.victorchu.snippets.tasks.TaskFinishReason.ABORTED;
import static info.victorchu.snippets.tasks.TaskFinishReason.SUCCESS;

@Slf4j
public class Task {

    private final ExecutorService executor;
    /**
     * Task Id
     */
    private final String id;

    private final TaskContext context;
    /**
     * All Stages in this task
     */
    private final List<Stage> stages;
    /**
     * ready stages
     */
    private final Queue<Stage> readyStages = new ConcurrentLinkedQueue<>();
    /**
     * stage pending dependencies count
     */
    private final Map<Stage, Integer> pendingDependencies = new ConcurrentHashMap<>();
    /**
     * completed stages count
     */
    private final AtomicInteger completedStages = new AtomicInteger(0);

    private final SettableFuture<TaskFinishReason> done = SettableFuture.create();

    private final AtomicBoolean isExecuting = new AtomicBoolean(false);

    private final List<TaskListener> listeners = new ArrayList<>();

    public Task(ExecutorService executor, String id, List<Stage> stages, List<TaskListener> listeners, TaskContext context) {
        this.id = id;
        this.stages = Collections.unmodifiableList(stages);
        this.executor = executor;
        this.context = context;
        // 初始化依赖计数
        for (Stage stage : stages) {
            int dependencyCount = stage.getDependencies().size();
            pendingDependencies.put(stage, dependencyCount);
            if (dependencyCount == 0) {
                readyStages.add(stage);
            }
        }
        this.listeners.addAll(listeners);
    }

    public boolean cancel() {
        if (!isExecuting.get()){
            return false;
        }
        return done.set(ABORTED);
    }

    public ListenableFuture<?> execute() {
        if (!isExecuting.compareAndSet(false, true)) {
            return done;
        }
        onTaskEvent(new TaskEvent.TaskStartEvent(this));
        schedule();
        MoreFutures.addCallback(done, new FutureCallback<TaskFinishReason>() {
            @Override
            public void onSuccess(TaskFinishReason result) {
                if(result == SUCCESS){
                    onTaskEvent(new TaskEvent.TaskSuccessEvent(Task.this));
                }else {
                    onTaskEvent(new TaskEvent.TaskAbortEvent(Task.this));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onTaskEvent(new TaskEvent.TaskFailedEvent(Task.this,t));
            }
        });
        return done;
    }

    private void onTaskEvent(TaskEvent event) {
        safeExecute(()->{
            for (TaskListener listener: listeners){
                quietlyRun(()->{
                    listener.onTaskEvent(event);
                },"TaskListener 运行异常:");
            }
        },executor);
    }


    private void quietlyRun(Runnable runnable,String message){
        try {
            runnable.run();
        }catch (Throwable t){
            log.error(message,t);
        }
    }

    private void schedule() {
        // 如果没有就绪的阶段，但还有未完成的任务，说明有循环依赖
        if (readyStages.isEmpty() && completedStages.get() < stages.size()) {
            done.setException(new RuntimeException("task调度异常, Circular dependency detected or no entry point"));
        }
        // 执行所有就绪的阶段
        while (!readyStages.isEmpty()) {
            Stage stage = readyStages.poll();
            Future<?> stageFuture= executor.submit(() -> {
                try {
                    onStageStart(stage);
                    stage.execute();
                    onStageCompleted(stage);
                } catch (Throwable e) {
                    onStageFailed(stage, e);
                }
            });
            // 如果Task执行失败，取消Stage 的执行
            MoreFutures.addCallback(done, new FutureCallback<TaskFinishReason>() {
                @Override
                public void onSuccess(TaskFinishReason result) {
                    if(result == ABORTED && !stageFuture.isDone()) {
                        onStageCanceled(stage,null);
                        stageFuture.cancel(true);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if(!stageFuture.isDone()) {
                        onStageCanceled(stage,t);
                        stageFuture.cancel(true);
                    }
                }
            });
        }
    }

    private void onStageEvent(List<StageListener> listeners,StageEvent event) {
        safeExecute(()->{
            for (StageListener listener: listeners){
                quietlyRun(()->{
                    listener.onStageEvent(event);
                },"StageListener 运行异常:");
            }
        },executor);
    }

    private void onStageCanceled(Stage stage,Throwable throwable) {
        if(throwable == null){
            log.info("Stage {} canceled due to Abort", stage);
        }else {
            log.info("Stage {} canceled due to failure", stage);
        }
        safeExecute(()->{
            onStageEvent(stage.getStageListeners(),new StageEvent.StageAbortEvent(stage,throwable));
        },executor);
    }

    private void onStageStart(Stage stage) {
        log.info("Stage {} started ", stage);
        safeExecute(()->{
            onStageEvent(stage.getStageListeners(),new StageEvent.StageStartEvent(stage));
        },executor);
    }

    private void onStageFailed(Stage failedStage, Throwable e) {
        log.error("Stage {} failed with exception:", failedStage, e);
        safeExecute(()->{
            onStageEvent(failedStage.getStageListeners(),new StageEvent.StageFailedEvent(failedStage,e));
        },executor);

        if(!done.isDone()) {
            done.setException(e);
        }
    }
    private void onStageCompleted(Stage completedStage) {
        log.info("Stage {} completed", completedStage);
        safeExecute(()->{
            onStageEvent(completedStage.getStageListeners(),new StageEvent.StageSuccessEvent(completedStage));
        },executor);

        // 更新依赖计数并检查是否有新的阶段就绪
        for (Stage dependent : completedStage.getDependents()) {
            int newCount = pendingDependencies.compute(dependent, (k, v) -> v - 1);
            if (newCount == 0) {
                readyStages.add(dependent);
            }
        }

        // 如果所有阶段都完成了，标记任务完成
        if (completedStages.incrementAndGet() == stages.size()) {
            done.set(SUCCESS);
            return;
        }

        // 如果有新的就绪阶段，继续执行
        if (!readyStages.isEmpty()) {
            readyStages.forEach(x->{
                log.info("Stage {} is ready to execute", x);
            });
            schedule();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor= Executors.newFixedThreadPool(4);
        // 创建任务
        TaskFactory factory = new TaskFactory("Task01", executor,new TaskContext());
        Stage stage1 = factory.newStage(() -> {
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "Stage 1");
        Stage stage2 = factory.newStage(() -> {
            try {
                Thread.sleep(2*10*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "Stage 2");
        Stage stage3 = factory.newStage(() -> {
            try {
                Thread.sleep(3*10*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Stage 3");
        Stage stage4 = factory.newStage(() -> {
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Stage 4");
        stage2.addDependency(stage1);
        stage3.addDependency(stage1);
        stage4.addDependency(stage2);
        stage4.addDependency(stage3);
        factory.addStages(stage1, stage2, stage3, stage4);
        Task task = factory.build();
        try {
            ListenableFuture<?> future = task.execute();
            future.get();
        } catch (Exception e) {
            log.info("Task finished failed",e);
        }
    }
}
