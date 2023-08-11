package info.victorchu.j8.jvm.mem;

public class JavaString02 {
    public static void main(String[] args) {
        String str = String.valueOf(6666666);
        str.intern();
        String str2 = "6666666";
        System.out.print(str == str2);
    }
}
