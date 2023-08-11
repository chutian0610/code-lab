package info.victorchu.tool.jvm.charlene.clazz.constant;

/**
 * utf-8 编码的字符串.
 * <pre>
 * CONSTANT_Utf8_info {
 *     u1 tag;
 *     u2 length;  utf8编码的字符串占用的字节数
 *     u1 bytes[length];  长度为length的 utf8 编码的字符串
 * }
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantUtf8Info extends AbstractConstantPoolInfo {
    private final String value;

    public ConstantUtf8Info(int tag, String value) {
        super(tag);
        this.value = value;
    }

    public String getUtf8() {
        return value;
    }

    @Override
    public String toString() {
        return "ConstantUtf8Info{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
