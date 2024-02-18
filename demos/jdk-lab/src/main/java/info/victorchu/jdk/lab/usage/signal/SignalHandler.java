package info.victorchu.jdk.lab.usage.signal;

import sun.misc.Signal;

import java.lang.management.ManagementFactory;

/**JDK 信号处理工具类.
 *
 */
public class SignalHandler {
    public static class Handler implements sun.misc.SignalHandler {

        private final sun.misc.SignalHandler prevHandler;

        Handler(String name) {
            // 注册对指定信号的处理
            prevHandler = Signal.handle(new Signal(name), this);
        }
        @Override
        public void handle(Signal signal) {
            // 信号量名称
            String name = signal.getName();
            // 信号量数值
            int number = signal.getNumber();

            // 当前进程名
            String currentThreadName = Thread.currentThread().getName();

            System.out.println("[Thread:"+currentThreadName + "] receved signal: " + name + " == kill -" + number);
            // 将signal 传递给 jvm 底层
            prevHandler.handle(signal);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        String pid = name.split("@")[0];
        System.out.println("Pid is:"+pid);
        final String[] SIGNALS = new String[]{ "TERM", "HUP", "INT" };
        for (String signalName : SIGNALS) {
            new Handler(signalName);
        }
        while(true) {
            Thread.sleep(1000);
        }
    }
}
