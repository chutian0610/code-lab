package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolMarkWord02 {
    public static void main(String[] args) {
        final A a = new A();
        ClassLayout layout = ClassLayout.parseInstance(a);

        System.out.println("**** Fresh object");
        System.out.println(layout.toPrintable());

        synchronized (a) {
            System.out.println("**** With the lock");
            System.out.println(layout.toPrintable());
        }

        System.out.println("**** After the lock");
        System.out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }

}
