package info.victorchu.tool.jvm.charlene.clazz;

import info.victorchu.tool.jvm.charlene.clazz.deserializer.ClassFileReader;

import java.io.IOException;

/**
 * Jvm class 文件的field 处理器.
 * <pre>
 * field_info {
 *     u2             access_flags; 字段修饰符
 *     u2             name_index;  对常量池的引用,代表字段的简单名称
 *     u2             descriptor_index; 对常量池的引用,代表字段的描述符，字段类型
 *     u2             attributes_count; 属性表长度
 *     attribute_info attributes[attributes_count]; 属性表
 * }
 * </pre>
 * @see com.sun.tools.classfile.Field
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class Field {
    public final AccessFlags accessFlags;
    public final int nameIndex;
    public final Descriptor descriptor;
//    public final Attributes attributes;

    public Field(ClassFileReader cr) throws IOException {
        accessFlags = new AccessFlags(cr);
        nameIndex = cr.readUnsignedShort();
        descriptor = new Descriptor(cr);

    }
}
