package info.victorchu.commontool.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author victorchu
 * @date 2022/8/5 22:14
 */
public final class ThreadDumpGenerator {

    private ThreadDumpGenerator() {
    }

    public static String dumpAllThreads() {
        StringBuilder s = new StringBuilder();
        s.append("Full thread dump ");
        return dump(getAllThreads(), s);
    }

    public static String dumpDeadlocks() {
        StringBuilder s = new StringBuilder();
        s.append("Deadlocked thread dump ");
        return dump(findDeadlockedThreads(), s);
    }

    private static String dump(ThreadInfo[] infos, StringBuilder s) {
        header(s);
        appendThreadInfos(infos, s);
        return s.toString();
    }

    public static ThreadInfo[] findDeadlockedThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (((ThreadMXBean) threadMXBean).isSynchronizerUsageSupported()) {
            long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
            if (deadlockedThreads == null || deadlockedThreads.length == 0) {
                return null;
            }
            return threadMXBean.getThreadInfo(deadlockedThreads, true, true);
        } else {
            long[] monitorDeadlockedThreads = threadMXBean.findMonitorDeadlockedThreads();
            return getThreadInfos(threadMXBean, monitorDeadlockedThreads);
        }
    }

    public static ThreadInfo[] getAllThreads() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean.isObjectMonitorUsageSupported()
                && threadMXBean.isSynchronizerUsageSupported()) {
            return threadMXBean.dumpAllThreads(true, true);
        }
        long[] allThreadIds = threadMXBean.getAllThreadIds();
        return getThreadInfos(threadMXBean, allThreadIds);
    }

    private static ThreadInfo[] getThreadInfos(ThreadMXBean threadMXBean, long[] threadIds) {
        if (threadIds == null || threadIds.length == 0) {
            return null;
        }
        return threadMXBean.getThreadInfo(threadIds, Integer.MAX_VALUE);
    }

    private static void header(StringBuilder s) {
        s.append(System.getProperty("java.vm.name"));
        s.append(" (");
        s.append(System.getProperty("java.vm.version"));
        s.append(" ");
        s.append(System.getProperty("java.vm.info"));
        s.append("):");
        s.append("\n\n");
    }

    private static void appendThreadInfos(ThreadInfo[] infos, StringBuilder s) {
        if (infos == null || infos.length == 0) {
            return;
        }
        for (ThreadInfo info : infos) {
            s.append(info);
        }
    }
}
