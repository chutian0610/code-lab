package info.victorchu.j8.usage.type.cinit.demo.order;

class Parent {
    static Sample staticSample = new Sample("Parent:静态成员staticSample初始化");
    static {
        System.out.println("Parent:类static块执行");
        Sample.init(staticSample,"Parent:类static块>>静态成员staticSample初始化");
    }

    {
        System.out.println("Parent:对象非静态块1执行");
    }
    Sample sampleField1 = new Sample("Parent:普通成员sampleField1初始化");
    final Sample  finalSample = new Sample("Parent:final成员finalSample初始化");

    Parent() {
        sampleField1 = new Sample("Parent:构造函数>>普通成员sampleField1初始化");
        viewValues();
        System.out.println("Parent:构造函数被调用");
    }

    //调用子类的override函数，访问子类未初始化的非静态成员变量
    public void viewValues() {

    }
}