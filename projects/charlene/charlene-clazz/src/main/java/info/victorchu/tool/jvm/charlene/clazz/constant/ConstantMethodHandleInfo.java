package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * 方法句柄.
 * <pre>
 * CONSTANT_MethodHandle_info {
 *     u1 tag;
 *     u1 reference_kind;   1-9,决定了方法句柄的类型
 *     u2 reference_index;  对常量池的有效索引
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantMethodHandleInfo extends AbstractConstantPoolInfo {
    private final RefKind referenceKind;
    private final int referenceIndex;
    public ConstantMethodHandleInfo(int tag, int referenceKind, int referenceIndex) {
        super(tag);
        this.referenceKind = RefKind.getRefkind(referenceKind);
        this.referenceIndex = referenceIndex;
    }

    public RefKind getReferenceKind() {
        return referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }

    @Override
    public String toString() {
        return "ConstantMethodHandleInfo{" +
                "referenceKind=" + referenceKind +
                ", referenceIndex=" + referenceIndex +
                "} " + super.toString();
    }
}
