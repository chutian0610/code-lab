package info.victorchu.j8.jvm.option;

/**
 * <pre>
 *   JVM对一些特定的异常类型做了Fast Throw优化，
 *   如果检测到在代码里某个位置连续多次抛出同一类型异常的话，C2会决定用Fast Throw方式来抛出异常，
 *   而异常Trace即详细的异常栈信息会被清空。
 *
 *   抛出了几千次带有详细异常栈信息的异常后，只会抛出java.lang.NullPointerException这种没有详细异常栈信息只有异常类型的异常信息。
 *   这就是Fast Throw优化后抛出的异常。
 *   如果我们配置了-XX:-OmitStackTraceInFastThrow，再次运行，就不会看到Fast Throw优化后抛出的异常，全是包含了详细异常栈的异常信息。
 * </pre>
 * @author victorchu
 * @date 2022/8/15 19:57
 */
public class TestOmitStackTraceInFastThrow {
    public static void main(String[] args) {
        int counter = 0;
        int c=0;
        while(true) {
            try {
                Object obj = null;
                /*
                 * If we cause the exception every time(= without this "if" statement),
                 * the optimization does not happen somehow.
                 * So, cause it conditionally.
                 */
                if(counter % 2 == 0) {
                    obj = new Object();
                }
                // Cause NullpointerException
                obj.getClass();
            }catch(NullPointerException e) {
                e.printStackTrace();
                if(e.getStackTrace() == null || e.getStackTrace().length==0 ){
                    c++;
                    if(c>2){
                        break;
                    }
                }
            }
            counter++;
        }
    }
}
