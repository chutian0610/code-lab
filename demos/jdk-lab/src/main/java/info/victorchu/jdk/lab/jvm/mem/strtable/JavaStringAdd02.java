package info.victorchu.jdk.lab.jvm.mem.strtable;

public class JavaStringAdd02 {
    
    public static void main(String[] args) {
        String str1 = new String("baseStr")+new String("01");
        String str2 = "base"+"Str"+"01";
        System.out.println(str2 == str1);
    }
}
