package info.victorchu.jvmtool.clazz.constant;

/**
 * 字符串类型字面量.
 * <pre>
 * CONSTANT_String_info {
 * u1 tag;
 * u2 string_index; 字符串字面量的索引
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantStringInfo extends AbstractConstantPoolInfo {
    private final int index;

    public ConstantStringInfo(int tag, int index) {
        super(tag);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "ConstantStringInfo{" +
                "index=" + index +
                "} " + super.toString();
    }
}
