package info.victorchu.snippets.tasks.pcfuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author victorchu
 * @date 2022/8/8 20:32
 */
@Slf4j
class TaskTest {
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        TaskLimitBlockQueue queue = new TaskLimitBlockQueue(16);
        TaskConsumer consumer = new TaskConsumer(queue);
        consumer.start();
        for (int i = 0; i < 25; i++) {
            Task task = new TaskImpl();
            if(queue.offer(task)){
                log.info("task{} offered",task.getName());
            }else{
                log.info("task{} drop",task.getName());
            }
        }
        Thread.sleep(3000);
        Task task = new TaskImpl();
        if(queue.offer(task)) {
            log.info("task{} offered",task.getName());
            task.get();
        }else {
            log.info("task{} drop",task.getName());
        }


    }
}