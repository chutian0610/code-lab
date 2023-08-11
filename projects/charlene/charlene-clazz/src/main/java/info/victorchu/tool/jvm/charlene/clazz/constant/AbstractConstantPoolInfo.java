package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * abstract constant pool item.
 * <pre>
 * cp_info {
 *     u1 tag;
 * }
 * </pre>
 * @see com.sun.tools.classfile.ConstantPool.CPInfo
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public abstract class AbstractConstantPoolInfo {

    private final int tag;

    public AbstractConstantPoolInfo(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
