package info.victorchu.signal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * jvm shutdown hook 演示
 */
public class TestShutdownHook {
    static Timer timer = new Timer("job-timer");
    static AtomicInteger count = new AtomicInteger(0);

    /**
     * hook线程
     */
    static class CleanWorkThread extends Thread{
        @Override
        public void run() {
            System.out.println("clean some work.");
            timer.cancel();
            try {
                System.out.println("try sleep 2s.");
                Thread.sleep(2 * 1000);
                System.out.println("sleep 2s.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        //将hook线程添加到运行时环境中去
        Runtime.getRuntime().addShutdownHook(new CleanWorkThread());
        System.out.println("ShutdownHook added");
        //简单模拟
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count.getAndIncrement();
                System.out.println("doing job " + count);
                if (count.get() == 3) {  //执行了3次后退出
                    System.exit(0);
                }
            }
        }, 0, 2 * 1000);
    }
}
