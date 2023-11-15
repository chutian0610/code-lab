package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;
import sun.misc.Contended;

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
        @sun.misc.Contended("first")
        int f;
        @sun.misc.Contended("first")
        int g;
        @sun.misc.Contended("last")
        int i;
        @sun.misc.Contended("last")
        int k;
    }
}
