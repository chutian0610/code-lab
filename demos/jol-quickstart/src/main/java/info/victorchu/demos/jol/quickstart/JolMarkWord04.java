package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class JolMarkWord04 {
   
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

        System.out.println("hashCode: " + Integer.toHexString(a.hashCode()));
        System.out.println(layout.toPrintable());

        synchronized (a) {
            System.out.println("**** With the second lock");
            System.out.println(layout.toPrintable());
        }

        System.out.println("**** After the second lock");
        System.out.println(layout.toPrintable());
    }

    public static class A {
        // no fields
    }
}
