package info.victorchu.jvmtool.clazz.constant;

/**
 * 类或接口的符号引用.
 *
 * <pre>
 * CONSTANT_Class_info {
 *  u1 tag;
 *  u2 name_index; 指向全限定类名常量项的索引
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantClassInfo extends AbstractConstantPoolInfo {
    private final int nameIndex;

    public ConstantClassInfo(int tag, int nameIndex) {
        super(tag);
        this.nameIndex = nameIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    @Override
    public String toString() {
        return "ConstantClassInfo{" +
                "nameIndex=" + nameIndex +
                "} " + super.toString();
    }
}
