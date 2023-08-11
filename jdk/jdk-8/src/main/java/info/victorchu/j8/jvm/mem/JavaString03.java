package info.victorchu.j8.jvm.mem;

public class JavaString03 {
    
    public static void main(String[] args) {
        char[] chars = new char[]{
            's','t','r','i','n','g'
        };
        String str = new String(chars, 0, chars.length);
        str.intern();
        String str2 = "string";
        System.out.print(str == str2);
    }
}
