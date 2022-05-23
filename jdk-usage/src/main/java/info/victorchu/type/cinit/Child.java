package info.victorchu.type.cinit;

public class Child extends Parent{
    static Sample staticChildSample = new Sample("Child:静态成员staticChildSample初始化");
    static {
        System.out.println("Child:类static块执行");
        Sample.init(staticChildSample,"Child:类static块>>静态成员staticChildSample初始化");
    }
    final Sample finalChildSample = new Sample("Child:final成员finalChildSample初始化");
    Child() {
        System.out.println("Child:构造函数被调用");
    }

    private int derive0 = 888;
    final private int derive1 = 888;
    final private Integer derive2 = 888;
    final private String derive3 = new String("Hello World");
    final private String derive4 = "Hello World";

    @Override
    public void viewValues() {
        System.out.println("子类成员变量derive0 = " + derive0);
        System.out.println("子类成员变量derive1 = " + derive1);
        System.out.println("子类成员变量derive2 = " + derive2);
        System.out.println("子类成员变量derive3 = " + derive3);
        System.out.println("子类成员变量derive4 = " + derive4);
        System.out.println("子类成员变量finalChildSample = " + finalChildSample);
    }

}