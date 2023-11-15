package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;
/**
 * 对齐填充的例子
 */
public class JolCase01 {
  public static void main(String[] args) {
    System.out.println(ClassLayout.parseClass(A.class).toPrintable());
  }

  public static class A {
    long f;
  }
}
