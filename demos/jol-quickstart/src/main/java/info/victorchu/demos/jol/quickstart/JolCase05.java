package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolCase05 {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(C.class).toPrintable());
    }
    public static class A {
        boolean a;
    }
    public static class B extends A {
        boolean b;
    }
    public static class C extends B {
        boolean c;
    }
}
