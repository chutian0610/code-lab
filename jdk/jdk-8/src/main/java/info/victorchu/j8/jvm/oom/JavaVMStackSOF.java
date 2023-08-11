package info.victorchu.j8.jvm.oom;

/**
 * VM option: -Xss256k
 */
public class JavaVMStackSOF {
    private int stackLength =1 ;

    public  void stackLength(){
        stackLength ++;
        stackLength();
    }

    public static void main(String[] args){
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        try{
            javaVMStackSOF.stackLength();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
