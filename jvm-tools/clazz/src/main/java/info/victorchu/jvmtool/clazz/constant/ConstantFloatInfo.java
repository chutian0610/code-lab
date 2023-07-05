package info.victorchu.jvmtool.clazz.constant;

/**
 * 浮点型字面量.
 * <pre>
 * CONSTANT_Float_info {
 *     u1 tag;
 *     u4 bytes; 高位在前的 float
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantFloatInfo extends AbstractConstantPoolInfo {
    private final float value;
    public ConstantFloatInfo(int tag, float value) {
        super(tag);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstantFloatInfo{" +
                "value=" + value +
                "} " + super.toString();
    }
}
