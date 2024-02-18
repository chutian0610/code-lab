package info.victorchu.jdk.lab.jvm.oom;

/**
 * VM option: -Xss20M
 */
public class JavaVMStackOOM{
    private void dontStop(){
        while(true){
        }
    }
    private void stackLeakByThread(){
        while(true){
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    dontStop();
                }
            });
            thread.start();
        }
    }
    public static void main(String[] args){
        JavaVMStackOOM javaVMStackOOM = new JavaVMStackOOM();
        javaVMStackOOM.stackLeakByThread();
    }
 }
