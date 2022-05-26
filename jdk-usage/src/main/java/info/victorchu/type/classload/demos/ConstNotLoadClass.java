package info.victorchu.type.classload.demos;

/**
 * <pre>
 * Classfile /Users/didi/IdeaProjects/code-lab/jdk-usage/target/classes/info/victorchu/type/classload/ConstNotLoadClass.class
 *   Last modified 2022-5-13; size 400 bytes
 *   MD5 checksum 0c5c7459d5ff1bafc1b0416f07071efd
 *   Compiled from "ConstNotLoadClass.java"
 * public class info.victorchu.type.classload.demos.ConstNotLoadClass
 *   minor version: 0
 *   major version: 52
 *   flags: ACC_PUBLIC, ACC_SUPER
 * Constant pool:
 *    #1 = Methodref          #3.#17         // java/lang/Object."<init>":()V
 *    #2 = Class              #18            // info/victorchu/type/classload/ConstNotLoadClass
 *    #3 = Class              #19            // java/lang/Object
 *    #4 = Utf8               hello
 *    #5 = Utf8               Ljava/lang/String;
 *    #6 = Utf8               ConstantValue
 *    #7 = String             #4             // hello
 *    #8 = Utf8               <init>
 *    #9 = Utf8               ()V
 *   #10 = Utf8               Code
 *   #11 = Utf8               LineNumberTable
 *   #12 = Utf8               LocalVariableTable
 *   #13 = Utf8               this
 *   #14 = Utf8               Linfo/victorchu/type/classload/ConstNotLoadClass;
 *   #15 = Utf8               SourceFile
 *   #16 = Utf8               ConstNotLoadClass.java
 *   #17 = NameAndType        #8:#9          // "<init>":()V
 *   #18 = Utf8               info/victorchu/type/classload/ConstNotLoadClass
 *   #19 = Utf8               java/lang/Object
 * {
 *   public static final java.lang.String hello;
 *     descriptor: Ljava/lang/String;
 *     flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL
 *     ConstantValue: String hello
 *
 *   public info.victorchu.type.classload.demos.ConstNotLoadClass();
 *     descriptor: ()V
 *     flags: ACC_PUBLIC
 *     Code:
 *       stack=1, locals=1, args_size=1
 *          0: aload_0
 *          1: invokespecial #1                  // Method java/lang/Object."<init>":()V
 *          4: return
 *       LineNumberTable:
 *         line 3: 0
 *       LocalVariableTable:
 *         Start  Length  Slot  Name   Signature
 *             0       5     0  this   Linfo/victorchu/type/classload/ConstNotLoadClass;
 * }
 * SourceFile: "ConstNotLoadClass.java"
 * </pre>
 *
 * 用于验证常量不会导致类加载。
 * @see TestClassLoad
 */
public class ConstNotLoadClass {
    public static final String hello = "hello";
}