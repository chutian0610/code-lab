package info.victorchu.jvm.j8.mem;

public class VMStack {
    private static int increaseByTen(int c){
        return c + 10;
    }
    public void test() {
        long c;
        int a, b;
        a = 1;
        b = 2;
        c = increaseByTen(a*b);
        c = c*(a+b);
    }
}
