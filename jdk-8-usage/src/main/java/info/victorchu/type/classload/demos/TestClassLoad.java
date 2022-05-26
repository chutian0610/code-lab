package info.victorchu.type.classload.demos;


/**
 * javap  -v -p  -c TestClassLoad
 * <pre>
 * 警告: 二进制文件TestClassLoad包含info.victorchu.type.classload.TestClassLoad
 * Classfile /Users/didi/IdeaProjects/code-lab/jdk-usage/target/classes/info/victorchu/type/classload/TestClassLoad.class
 *   Last modified 2022-5-13; size 649 bytes
 *   MD5 checksum 5064752767ef5aa385af2cc06c61c242
 *   Compiled from "TestClassLoad.java"
 * public class info.victorchu.type.classload.demos.TestClassLoad
 *   minor version: 0
 *   major version: 52
 *   flags: ACC_PUBLIC, ACC_SUPER
 * Constant pool:
 *    #1 = Methodref          #7.#21         // java/lang/Object."<init>":()V
 *    #2 = Fieldref           #22.#23        // java/lang/System.out:Ljava/io/PrintStream;
 *    #3 = Class              #24            // info/victorchu/type/classload/ConstNotLoadClass
 *    #4 = String             #25            // hello
 *    #5 = Methodref          #26.#27        // java/io/PrintStream.println:(Ljava/lang/String;)V
 *    #6 = Class              #28            // info/victorchu/type/classload/TestClassLoad
 *    #7 = Class              #29            // java/lang/Object
 *    #8 = Utf8               <init>
 *    #9 = Utf8               ()V
 *   #10 = Utf8               Code
 *   #11 = Utf8               LineNumberTable
 *   #12 = Utf8               LocalVariableTable
 *   #13 = Utf8               this
 *   #14 = Utf8               Linfo/victorchu/type/classload/TestClassLoad;
 *   #15 = Utf8               main
 *   #16 = Utf8               ([Ljava/lang/String;)V
 *   #17 = Utf8               args
 *   #18 = Utf8               [Ljava/lang/String;
 *   #19 = Utf8               SourceFile
 *   #20 = Utf8               TestClassLoad.java
 *   #21 = NameAndType        #8:#9          // "<init>":()V
 *   #22 = Class              #30            // java/lang/System
 *   #23 = NameAndType        #31:#32        // out:Ljava/io/PrintStream;
 *   #24 = Utf8               info/victorchu/type/classload/ConstNotLoadClass
 *   #25 = Utf8               hello
 *   #26 = Class              #33            // java/io/PrintStream
 *   #27 = NameAndType        #34:#35        // println:(Ljava/lang/String;)V
 *   #28 = Utf8               info/victorchu/type/classload/TestClassLoad
 *   #29 = Utf8               java/lang/Object
 *   #30 = Utf8               java/lang/System
 *   #31 = Utf8               out
 *   #32 = Utf8               Ljava/io/PrintStream;
 *   #33 = Utf8               java/io/PrintStream
 *   #34 = Utf8               println
 *   #35 = Utf8               (Ljava/lang/String;)V
 * {
 *   public info.victorchu.type.classload.demos.TestClassLoad();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC
 *     Code:
 *       stack=1, locals=1, args_size=1
 *          0: aload_0
 *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *          4: return
 *       LineNumberTable:
 *         line 4: 0
 *       LocalVariableTable:
 *         Start  Length  Slot  Name   Signature
 *             0       5     0  this   Linfo/victorchu/type/classload/TestClassLoad;
 *
 *   public static void main(java.lang.String[]);
 *     descriptor: ([Ljava/lang/String;)V
 *     flags: ACC_PUBLIC, ACC_STATIC
 *     Code:
 *       stack=2, locals=1, args_size=1
 *          0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *          3: ldc           #4                  // String hello
 *          5: invokevirtual #5                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
 *          8: return
 *       LineNumberTable:
 *         line 6: 0
 *         line 9: 8
 *       LocalVariableTable:
 *         Start  Length  Slot  Name   Signature
 *             0       9     0  args   [Ljava/lang/String;
 * }
 * SourceFile: "TestClassLoad.java"
 * </pre>
 * 用于验证常量不会导致类加载。
 * @see ConstNotLoadClass
 */

public class TestClassLoad {
    public static void main(String[] args){
        System.out.println(ConstNotLoadClass.hello);
        // 编译期已经把ConstantClass.hello 转化为test类的常量池中常量
        // 没有输出
    }
}