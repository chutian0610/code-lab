package info.victorchu.j8.jmx;


import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;
import java.util.Arrays;

/**
 * 监控线程的内存分配
 * @author victorchu

 */
public class ThreadMemory {
    public static void main(String[] args)
    {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        System.out.printf("supported=%s enabled=%s%n",
                threadMXBean.isThreadAllocatedMemorySupported(),
                threadMXBean.isThreadAllocatedMemoryEnabled());

        for (int i = 1; i <= 5; i++) {
            int size = i;
            new Thread(() -> {
                long threadId = Thread.currentThread().getId();

                long start = threadMXBean.getThreadAllocatedBytes(threadId);
                System.out.printf("thread %s start: allocated=%s%n", threadId, start);

                int actual = 0;
                for (int j = 0; j < 2000; j++) {
                    byte[] bytes = new byte[size * 500];
                    Arrays.fill(bytes, (byte) 42);
                    actual += bytes.length;
                    bytes = null;
                }
                // add jvm option -XX:+PrintGCDetails
                System.gc();
                long end = threadMXBean.getThreadAllocatedBytes(threadId);
                System.out.printf("thread %s end: allocated=%s delta=%s actual=%s %n", threadId, end, end - start, actual);
            }).start();
        }
    }
}
