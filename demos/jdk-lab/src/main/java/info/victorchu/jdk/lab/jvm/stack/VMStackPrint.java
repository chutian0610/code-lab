package info.victorchu.jdk.lab.jvm.stack;

import java.util.Collections;

/**
 * @author victorchu
 */
public class VMStackPrint
{
    public static void main(String[] args)
    {
        factorial(3);
    }

    static void printStackFrame()
    {
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTraces.length; i++) {
            String prefix = i <= 2 ? "" : String.join("", Collections.nCopies(i - 3, "  ")) + "└─";
            System.out.println(prefix + stackTraces[i].toString());
        }
    }

    static int factorial(int n)
    {
        printStackFrame();
        if (n <= 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
