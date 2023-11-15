package info.victorchu.demos.jol.quickstart;

import java.io.PrintWriter;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class JolAdvance03 {
    static volatile Object sink;

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out, true);

        Object o = new Object();

        ClassLayout layout = ClassLayout.parseInstance(o);

        long lastAddr = VM.current().addressOf(o);
        pw.printf("*** Fresh object is at %x%n", lastAddr);
        System.out.println(layout.toPrintable());

        int moves = 0;
        for (int i = 0; i < 100000; i++) {
            long cur = VM.current().addressOf(o);
            if (cur != lastAddr) {
                moves++;
                pw.printf("*** Move %2d, object is at %x%n", moves, cur);
                System.out.println(layout.toPrintable());
                lastAddr = cur;
            }

            // make garbage
            for (int c = 0; c < 10000; c++) {
                sink = new Object();
            }
        }

        long finalAddr = VM.current().addressOf(o);
        pw.printf("*** Final object is at %x%n", finalAddr);
        System.out.println(layout.toPrintable());

        pw.close();
    }
}
