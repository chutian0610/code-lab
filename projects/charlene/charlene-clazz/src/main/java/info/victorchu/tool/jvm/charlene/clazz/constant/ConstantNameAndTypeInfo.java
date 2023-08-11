package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * 字段或方法的部分符号引用.
 * <pre>
 * CONSTANT_NameAndType_info {
 *     u1 tag;
 *     u2 name_index; 指向该字段或方法名称常量项
 *     u2 descriptor_index; 指向该字段或方法描述符常量项
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantNameAndTypeInfo extends AbstractConstantPoolInfo {
    private final int nameIndex;
    private final int descriptorIndex;
    public ConstantNameAndTypeInfo(int tag, int nameIndex, int descriptorIndex) {
        super(tag);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    @Override
    public String toString() {
        return "ConstantNameAndTypeInfo{" +
                "nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                "} " + super.toString();
    }
}
