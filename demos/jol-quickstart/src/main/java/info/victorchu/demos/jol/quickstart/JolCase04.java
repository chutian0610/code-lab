package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolCase04 {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(C.class).toPrintable());
    }

    public static class A {
        long a;
    }

    public static class B extends A {
        int b;
    }

    public static class C extends B {
        long c;
        int d;
    }
}
