package info.victorchu.demos.jol.quickstart;

import jdk.internal.vm.annotation.Contended;
import org.openjdk.jol.info.ClassLayout;

/**
 * 设置jvm参数为 -XX:-RestrictContended 
 */
public class JolCase07 {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(B.class).toPrintable());
    }

    public static class A {
        int a;
        int b;
        @Contended
        int c;
        int d;
    }

    public static class B extends A {
        int e;
        @Contended("first")
        int f;
        @Contended("first")
        int g;
        @Contended("last")
        int i;
        @Contended("last")
        int k;
    }
}
