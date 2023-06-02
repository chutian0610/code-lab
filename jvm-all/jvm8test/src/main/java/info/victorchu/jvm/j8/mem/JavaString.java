package info.victorchu.jvm.j8.mem;

/**
 * 查看字节码中
 * 1. JVM对String的存放位置，运行时常量池,StringTable和堆中
 * 2. JVM中”+“的处理
 * <pre>
 * cd jvm-all/jvm8test/target/classes/info/victorchu/jvm/j8/mem
 * javap -verbose JavaString
 * </pre>
 */
public class JavaString {
    public static void main(String[] args) {
        String baseStr = "baseStr";
        final String baseFinalStr = "baseStr";

        String str1 = "baseStr01";
        String str2 = "baseStr" + "01";
        String str3 = baseStr + "01";
        String str4 = baseFinalStr + "01";
        String str5 = new String("baseStr01").intern();

        System.out.println(str1 == str2);
        System.out.println(str1 == str3);
        System.out.println(str1 == str4);
        System.out.println(str1 == str5);
    }
}
