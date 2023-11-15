package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;
/**
 * 字段重排序的例子
 */
public class JolCase02 {
      public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(A.class).toPrintable());
    }

    public static class A {
        boolean bo1;
        byte b1;
        char c1, c2;
        double d1, d2;
        float f1, f2;
        int i1, i2;
        long l1, l2;
        short s1, s2;
    }
}
