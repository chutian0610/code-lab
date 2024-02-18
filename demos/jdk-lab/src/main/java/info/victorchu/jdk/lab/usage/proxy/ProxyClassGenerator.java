package info.victorchu.jdk.lab.usage.proxy;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class ProxyClassGenerator {

    public static void generateClass(){
        Subject realSubject = new RealSubject();
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0",
                RealSubject.class.getInterfaces());
        String path= System.getProperty("user.dir");

        FileOutputStream out = null;

        try {
            System.out.println(path);
            out = new FileOutputStream(path+"/$Proxy0.class");
            out.write(classFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        generateClass();
    }
}
