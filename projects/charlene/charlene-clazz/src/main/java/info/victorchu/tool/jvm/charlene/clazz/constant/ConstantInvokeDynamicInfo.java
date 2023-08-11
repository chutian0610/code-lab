package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * 动态方法调用点.
 * <pre>
 * CONSTANT_InvokeDynamic_info {
 *     u1 tag;
 *     u2 bootstrap_method_attr_index; 值必须是对当前 class中引导方法表 bootstrap_method[] 数组的有效索引
 *     u2 name_and_type_index; 对常量池的有效索引，CONSTANT_NameAndType_info 类型
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantInvokeDynamicInfo extends AbstractConstantPoolInfo {
    private final int bootstrapMethodAttrIndex;
    private final int nameAndTypeIndex;
    public ConstantInvokeDynamicInfo(int tag, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(tag);
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    @Override
    public String toString() {
        return "ConstantInvokeDynamicInfo{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                "} " + super.toString();
    }
}
