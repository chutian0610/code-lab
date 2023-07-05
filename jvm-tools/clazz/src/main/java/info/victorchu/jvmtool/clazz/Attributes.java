package info.victorchu.jvmtool.clazz;

import info.victorchu.jvmtool.clazz.deserializer.ClassFileReader;
import info.victorchu.jvmtool.clazz.exception.ClassFileFormatException;
import info.victorchu.jvmtool.clazz.attribute.AbstractAttributeInfo;
import info.victorchu.jvmtool.clazz.attribute.ConstantValueAttribute;
import info.victorchu.jvmtool.clazz.exception.ConstantPoolException;

import java.io.DataInput;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Jvm class 文件 attributes
 *
 * <a herf="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7">Attributes</a>
 * <pre>
 * Attribute:
 *    1. 5个属性用于JVM正确解释文件
 *         ConstantValue
 *         Code
 *         StackMapTable
 *         Exceptions
 *         BootstrapMethods
 *    2. 12 个属性用于 Java SE 平台的类库正确解释文件
 *         InnerClasses
 *         EnclosingMethod
 *         Synthetic
 *         Signature
 *         RuntimeVisibleAnnotations
 *         RuntimeInvisibleAnnotations
 *         RuntimeVisibleParameterAnnotations
 *         RuntimeInvisibleParameterAnnotations
 *         RuntimeVisibleTypeAnnotations
 *         RuntimeInvisibleTypeAnnotations
 *         AnnotationDefault
 *         MethodParameters
 *   3. 六个属性常用于工具
 *         SourceFile
 *         SourceDebugExtension
 *         LineNumberTable
 *         LocalVariableTable
 *         LocalVariableTypeTable
 *         Deprecated
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class Attributes implements Iterable<AbstractAttributeInfo> {
    public final AbstractAttributeInfo[] attrs;
    public final Map<String, AbstractAttributeInfo> map;

    Attributes(ClassFileReader cr,ConstantPool constantPool) throws IOException {
        map = new HashMap<>();
        int attrs_count = cr.readUnsignedShort();
        attrs = new AbstractAttributeInfo[attrs_count];
        for (int i = 0; i < attrs_count; i++) {
            AbstractAttributeInfo attr = AbstractAttributeInfo.read(cr,constantPool);
            attrs[i] = attr;
            try {
                map.put(constantPool.getUtf8Info(attr.attributeNameIndex).getUtf8(), attr);
            } catch (ConstantPoolException e) {
                // don't enter invalid names in map
            }
        }
    }
    public Attributes(ConstantPool constantPool, AbstractAttributeInfo[] attrs) {
        this.attrs = attrs;
        map = new HashMap<>();
        for (AbstractAttributeInfo attr : attrs) {
            try {
                map.put(constantPool.getUtf8Info(attr.attributeNameIndex).getUtf8(), attr);
            } catch (ConstantPoolException e) {
                // don't enter invalid names in map
            }
        }
    }

    public Attributes(Map<String, AbstractAttributeInfo> attributes) {
        this.attrs = attributes.values().toArray(new AbstractAttributeInfo[attributes.size()]);
        map = attributes;
    }

    @Override
    public Iterator<AbstractAttributeInfo> iterator() {
        return Arrays.asList(attrs).iterator();
    }


    public AbstractAttributeInfo get(int index) {
        return attrs[index];
    }

    public AbstractAttributeInfo get(String name) {
        return map.get(name);
    }
}