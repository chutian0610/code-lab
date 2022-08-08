package info.victorchu.commontool.tasks.pcfuture;

import java.util.concurrent.Future;

/**
 * @author victorchu
 * @date 2022/8/5 22:24
 */
public interface Task extends Future<Object> {

    /**
     * task 权重
     * @return
     */
    default Integer getWeight(){
        return 1;
    }

    /**
     * 获取任务名
     * @return
     */
    String getName();

    /**
     * 任务进入队列时触发
     */
    void onPut();

    /**
     * 任务被消费时触发
     * @param future
     */
    void onTake(Future<?> future);

    /**
     * 设置任务的执行结果
     * @param v
     */
    void setResult(Object v);

    /**
     * 设置任务的执行异常
     * @param v
     */
    void setException(Throwable v);

}