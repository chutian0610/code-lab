package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolCase06 {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(Throwable.class).toPrintable());
        System.out.println(ClassLayout.parseClass(Class.class).toPrintable());
    }
}
