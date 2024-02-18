package info.victorchu.jdk.lab.jvm.oom;

/**
 * 测试 stackoverflow
 * VM option: -Xss256k
 */
public class JavaVMStackSOF {
    private int stackLength =1 ;

    public  void stackLength(){
        ++ stackLength;
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
