package info.victorchu.jvm.j8.mem;

public class JavaString01 {
    public static void main(String[] args){
        String str1 = "string";
        String str2 = new String("string");
        String str3 = str2.intern();

        System.out.println(str1==str2);
        System.out.println(str1==str3);
    }
}
