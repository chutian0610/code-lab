package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;

public class JolArray01 {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new int[8]).toPrintable()); 
    }
}
