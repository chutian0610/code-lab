package info.victorchu.cinit;

public class InitialOrderTest {
    static SampleClass sam = new SampleClass("静态成员sam初始化");
    SampleClass classSam1 = new SampleClass("普通成员classSam1初始化");
    static {
        System.out.println("类 static块执行");
        if (sam == null) {
            System.out.println("sam is null");
        }
        sam = new SampleClass("静态块内初始化sam成员变量");
    }

    SampleClass classSam2 = new SampleClass("普通成员classSam2初始化");

    InitialOrderTest() {
        System.out.println("InitialOrderTest默认构造函数被调用");
    }

    public static void main(String[] args) {
        // 创建第1个主类对象
        System.out.println("第1个主类对象：");
        InitialOrderTest ts = new InitialOrderTest();

        // 创建第2个主类对象
        System.out.println("第2个主类对象：");
        InitialOrderTest ts2 = new InitialOrderTest();

        // 查看两个主类对象的静态成员：
        System.out.println("2个主类对象的静态对象：");
        System.out.println("第1个主类对象, 静态成员sam.s: " + ts.sam);
        System.out.println("第2个主类对象, 静态成员sam.s: " + ts2.sam);
    }
}

class SampleClass {
    // SampleClass 不能包含任何主类InitialOrderWithoutExtend的成员变量
    // 否则导致循环引用，循环初始化，调用栈深度过大
    // 抛出 StackOverFlow 异常

    String s;

    SampleClass(String s) {
        this.s = s;
        System.out.println(s);
    }

    SampleClass() {
        System.out.println("SampleClass默认构造函数被调用");
    }

    @Override
    public String toString() {
        return this.s;
    }
}