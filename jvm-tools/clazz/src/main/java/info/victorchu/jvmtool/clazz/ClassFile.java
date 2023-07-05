package info.victorchu.jvmtool.clazz;

import info.victorchu.jvmtool.clazz.deserializer.ClassFileReader;
import info.victorchu.jvmtool.clazz.exception.ClassFileFormatException;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

/**
 * jvm class 文件结构模型.
 * class 文件结构如下:<br/>
 * <pre>
 * ClassFile {
 *      u4             magic; 魔数
 *      u2             minor_version; 次版本号
 *      u2             major_version; 主版本号
 *      u2             constant_pool_count; 常量池容量(计数从1开始)
 *      cp_info        constant_pool[constant_pool_count-1]; 常量池
 *      u2             access_flags; 访问标志
 *      u2             this_class; 类索引
 *      u2             super_class; 父类索引
 *      u2             interfaces_count; 接口计数器
 *      u2             interfaces[interfaces_count]; 接口表
 *      u2             fields_count; 字段表计数
 *      field_info     fields[fields_count]; 字段表
 *      u2             methods_count; 方法表计数
 *      method_info    methods[methods_count]; 方法表
 *      u2             attributes_count; 属性表计数
 *      attribute_info attributes[attributes_count]; 属性表
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ClassFile {
    /**
     * class 文件头 默认魔数值.
     */

    public static final int CLASS_MAGIC_NUMBER = 0xCAFEBABE;
    public final int magic;
    public final int minorVersion;
    public final int majorVersion;
    public final ConstantPool constantPool;
    public final AccessFlags accessFlags;
    public final int thisClass;
    public final int superClass;
    public final int[] interfaces;
    public final Field[] fields;
//    public final Method[] methods;
//    public final Attributes attributes;

    ClassFile(InputStream in) throws IOException {
        ClassFileReader cr = new ClassFileReader(in);
        magic = cr.readInt();
        minorVersion = cr.readUnsignedShort();
        majorVersion = cr.readUnsignedShort();
        checkMagicNumber(magic);
        constantPool = new ConstantPool(cr);
        accessFlags = new AccessFlags(cr);
        thisClass = cr.readUnsignedShort();
        superClass = cr.readUnsignedShort();

        int interfaces_count = cr.readUnsignedShort();
        interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++){
            interfaces[i] = cr.readUnsignedShort();
        }

        int fields_count = cr.readUnsignedShort();
        fields = new Field[fields_count];
        for (int i = 0; i < fields_count; i++)
            fields[i] = new Field(cr);
    }

    /**
     * check magic number of DataInput. if validated , must equals ClassFile.classMagicNumber
     * @param magic 魔数
     */
    private static void checkMagicNumber(int magic) {
        if (magic != ClassFile.CLASS_MAGIC_NUMBER) {
            throw new ClassFileFormatException("Invalid class file format，wrong magic number！");
        }
    }

}