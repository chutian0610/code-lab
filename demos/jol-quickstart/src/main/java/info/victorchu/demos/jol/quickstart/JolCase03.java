package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolCase03 {
     public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(C.class).toPrintable());
    }

    public static class A {
        int a;
    }

    public static class B extends A {
        int b;
    }

    public static class C extends B {
        int c;
    }
}
