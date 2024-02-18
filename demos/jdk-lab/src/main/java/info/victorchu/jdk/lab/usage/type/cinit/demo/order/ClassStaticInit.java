package info.victorchu.jdk.lab.usage.type.cinit.demo.order;

public class ClassStaticInit {

    public static void main(String[] args) {
        staticFunction();
    }

    static ClassStaticInit st = new ClassStaticInit();

    static {   //静态代码块
        System.out.println("1");
    }

    {       // 实例代码块
        System.out.println("2");
    }

    ClassStaticInit() {    // 实例构造器
        System.out.println("3");
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {   // 静态方法
        System.out.println("4");
    }

    int a = 110;    // 实例变量
    static int b = 112;     // 静态变量
}