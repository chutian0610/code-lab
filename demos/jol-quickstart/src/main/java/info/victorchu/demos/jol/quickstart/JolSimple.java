package info.victorchu.demos.jol.quickstart;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author victorchu
 */
public class JolSimple01
{
    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(Object.class).toPrintable());
    }
}
