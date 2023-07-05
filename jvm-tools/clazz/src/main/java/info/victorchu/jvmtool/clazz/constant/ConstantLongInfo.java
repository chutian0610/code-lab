package info.victorchu.jvmtool.clazz.constant;

/**
 * 长整型字面量.
 * <pre>
 * CONSTANT_Long_info {
 * u1 tag;
 * u8 bytes; 高位在前的 long
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantLongInfo extends AbstractConstantPoolInfo {
    private final long value;

    public ConstantLongInfo(int tag, long value) {
        super(tag);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstantLongInfo{" +
                "value=" + value +
                "} " + super.toString();
    }
}
