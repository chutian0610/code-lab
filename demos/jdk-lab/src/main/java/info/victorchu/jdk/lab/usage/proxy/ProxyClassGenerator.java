package info.victorchu.jdk.lab.usage.proxy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyClassGenerator {

    public static void generateClass()
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        Subject realSubject = new RealSubject();

        // jdk 11 之后，ProxyGenerator是final，无法直接引用
        // byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0",
        // RealSubject.class.getInterfaces());

        Class cl = Class.forName("java.lang.reflect.ProxyGenerator");
        Method m = cl.getDeclaredMethod("generateProxyClass", String.class, Class[].class);
        m.setAccessible(true);
        byte[] classFile = (byte[]) m.invoke(null, "$Proxy0", RealSubject.class.getInterfaces());

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

    public static void main(String[] args)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
    {
        generateClass();
    }
}
