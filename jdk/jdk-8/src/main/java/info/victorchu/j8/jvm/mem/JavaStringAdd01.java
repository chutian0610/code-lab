package info.victorchu.j8.jvm.mem;

public class JavaStringAdd01 {
    
    public static void main(String[] args) {
        String str1 = "baseStr01";
        String str2 = "base"+"Str"+"01";
        String str3 = "baseStr"+new String("01");
        System.out.println(str2 == str1);
        System.out.println(str2 == str3);
    }
}
