package info.victorchu.jdk.lab.usage.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyClient {
    public static void main(String[] args) {

        Subject realSubject = new RealSubject();
        InvocationHandler handler = new MyInvocationHandler(realSubject);

        /*
         * 通过Proxy的newProxyInstance方法来创建我们的代理对象
         * 第一个参数 handler.getClass().getClassLoader() ，我们这里使用handler这个类的ClassLoader对象来加载我们的代理对象
         * 第二个参数realSubject.getClass().getInterfaces()，我们这里为代理对象提供的接口是真实对象所实行的接口，表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
         * 第三个参数handler， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
         */
        Subject subject = (Subject) Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
                .getClass().getInterfaces(), handler);
        System.out.println(subject.getClass().getCanonicalName());
        subject.logIn();
        subject.playGames();
        subject.logOut();
    }
}
