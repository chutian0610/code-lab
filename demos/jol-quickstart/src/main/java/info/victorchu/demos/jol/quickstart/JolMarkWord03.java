package info.victorchu.demos.jol.quickstart;

import java.util.concurrent.TimeUnit;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class JolMarkWord03 {
    public static void main(String[] args) throws Exception {
        final A a = new A();

        ClassLayout layout = ClassLayout.parseInstance(a);

        System.out.println("**** Fresh object");
        System.out.println(layout.toPrintable());

        Thread t = new Thread(() -> {
            synchronized (a) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        });

        t.start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println("**** Before the lock");
        System.out.println(layout.toPrintable());

        synchronized (a) {
            System.out.println("**** With the lock");
            System.out.println(layout.toPrintable());
        }

        System.out.println("**** After the lock");
        System.out.println(layout.toPrintable());

        System.gc();

        System.out.println("**** After System.gc()");
        System.out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }
}
