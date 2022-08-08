package info.victorchu.commontool.tasks.pcfuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 用户提交任务时会阻塞提交线程
 * @author victorchu
 * @date 2022/8/8 19:56
 */
@Slf4j
public class CallerBlocksPolicy implements RejectedExecutionHandler {

    private final long maxWait;

    /**
     * Construct instance based on the provided maximum wait time.
     * @param maxWait The maximum time to wait for a queue slot to be available, in milliseconds.
     */
    public CallerBlocksPolicy(long maxWait) {
        this.maxWait = maxWait;
    }
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                BlockingQueue<Runnable> queue = executor.getQueue();
                if (log.isDebugEnabled()) {
                    log.debug("Attempting to queue task execution for " + this.maxWait + " milliseconds");
                }
                if(maxWait>0) {
                    if (!queue.offer(r, this.maxWait, TimeUnit.MILLISECONDS)) {
                        throw new RejectedExecutionException("Max wait time expired to queue task");
                    }
                }else {
                    queue.put(r);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Task execution queued");
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException("Interrupted", e);
            }
        }
        else {
            throw new RejectedExecutionException("Executor has been shut down");
        }
    }

}
