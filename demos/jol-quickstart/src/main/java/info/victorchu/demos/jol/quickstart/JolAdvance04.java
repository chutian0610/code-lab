package info.victorchu.demos.jol.quickstart;

import java.io.PrintWriter;

import org.openjdk.jol.info.GraphLayout;

public class JolAdvance04 {
    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out, true);

        Integer[] arr = new Integer[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = i + 256; // boxing outside of Integer cache
        }

        String last = null;
        for (int c = 0; c < 100; c++) {
            String current = GraphLayout.parseInstance((Object) arr).toPrintable();

            if (last == null || !last.equalsIgnoreCase(current)) {
                pw.println(current);
                last = current;
            }
            // 显式触发GC
            System.gc();
        }

        pw.close();
    }
}
