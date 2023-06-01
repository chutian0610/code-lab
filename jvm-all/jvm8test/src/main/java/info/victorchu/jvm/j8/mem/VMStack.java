package info.victorchu.jvm.j8.mem;

public class VMStack {
    private static int increaseByTen(int c){
        return c + 10;
    }

    private static int multiplyByTen(int c){
        return c * 10;
    }

    public static void main(String[] args) {
        int a, b, c;
        a = 1;
        b = 2;
        c = increaseByTen(a*b);
        c = multiplyByTen(c)*(a+b);
    }
}
