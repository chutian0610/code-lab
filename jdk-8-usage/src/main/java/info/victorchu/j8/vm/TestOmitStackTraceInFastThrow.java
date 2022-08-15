package info.victorchu.j8.vm;

/**
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
