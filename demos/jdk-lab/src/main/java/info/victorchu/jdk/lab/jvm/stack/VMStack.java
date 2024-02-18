package info.victorchu.jdk.lab.jvm.stack;
public class VMStack {
    
    private static int increaseByTen(int a, int b){
        return a*b + 10; 
    }
    public void test() {
        long c;
        int a, b;
        a = 1;
        b = 2;
        c = increaseByTen(a, b);
        c = c*(a+b);
    }
}
