package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * 方法的符号引用.
 * <pre>
 * CONSTANT_Methodref_info {
 * u1 tag;
 * u2 class_index; 指向声明方法的类 CONSTANT_Class_info
 * u2 name_and_type_index; 指向字段描述符 CONSTANT_NameAndType_info的索引项
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantMethodRefInfo extends AbstractConstantPoolInfo {
    private final int classIndex;
    private final int nameAndTypeIndex;

    public ConstantMethodRefInfo(int tag, int classIndex, int nameAndTypeIndex) {
        super(tag);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    @Override
    public String toString() {
        return "ConstantMethodRefInfo{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                "} " + super.toString();
    }
}
