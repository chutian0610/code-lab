package info.victorchu.commontool.tasks.pcfuture;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 带资源限制的阻塞队列
 * @author victorchu
 * @date 2022/8/8 14:19
 */
public class TaskLimitBlockQueue extends LinkedBlockingQueue<Task> {

    public int availablePermits() {
        return semaphore.availablePermits();
    }

    /**
     * 信号量。用于资源限制
     */
    private volatile Semaphore semaphore;
    private final Integer permits;

    public TaskLimitBlockQueue(int permits) {
        semaphore = new Semaphore(permits);
        this.permits = permits;
    }


    @Override
    public void put(Task task) throws InterruptedException {
        semaphore.acquire(task.getWeight());
        super.put(task);
        task.onPut();
    }

    @Override
    public boolean offer(Task task, long timeout, TimeUnit unit) throws InterruptedException {
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        if(semaphore.tryAcquire(task.getWeight(),timeout,unit)){
            long nanos = deadline - System.nanoTime();
            if (nanos <= 0L) {
                semaphore.release(task.getWeight());
                return false;
            }else {
                if(super.offer(task, nanos, TimeUnit.NANOSECONDS)){
                    task.onPut();
                    return true;
                }
                semaphore.release(task.getWeight());
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean offer(Task task) {
        if(semaphore.tryAcquire(task.getWeight())){
            if(super.offer(task)){
                task.onPut();
                return true;
            }else {
                semaphore.release(task.getWeight());
                return false;
            }
        }
        return false;
    }

    @Override
    public Task take() throws InterruptedException {
        Task task= super.take();
        semaphore.release(task.getWeight());
        return task;
    }

    @Override
    public Task poll(long timeout, TimeUnit unit) throws InterruptedException {
        Task task= super.poll(timeout, unit);
        if(task !=null){
            semaphore.release(task.getWeight());
        }
        return task;
    }

    @Override
    public Task poll() {
        Task task=  super.poll();
        if(task !=null){
            semaphore.release(task.getWeight());
        }
        return task;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        Task task = (Task) o;
        if(super.remove(o)){
            semaphore.release(task.getWeight());
            return true;
        }
        return false;
    }

    @Override
    public synchronized void clear() {
        super.clear();
        semaphore = new Semaphore(permits);
    }
}

