package info.victorchu.tool.jvm.charlene.clazz.attribute;

/**
 * 代表了Jvm class 文件中类型是 ConstantValue 的 attribute.
 *
 * <p>ConstantValue类型的attribute结构如下:</p>
 * <pre>
 * ConstantValue_attribute {
 *     u2 attribute_name_index;
 *     u4 attribute_length;
 *     u2 constantvalue_index; // constant pool 中的有效索引
 * }
 * </pre>
 * <p>上面结构中的constantvalue_index类型如下:</p>
 *
 * <table border="1">
 *  <caption>constantvalue_index</caption>
 *  <tr>
 *      <th>Field Type</th> <th>Entry Type</th>
 *  </tr>
 *  <tr>
 *      <td>long </td> <td>CONSTANT_Long</td>
 *  </tr>
 *  <tr>
 *      <td>float</td> <td>CONSTANT_Float</td>
 *  </tr>
 *  <tr>
 *      <td>double</td> <td>CONSTANT_Double</td>
 *  </tr>
 *  <tr>
 *      <td>int, short, char, byte, boolean</td> <td>CONSTANT_Integer</td>
 *  </tr>
 *  <tr>
 *      <td>String</td> <td>CONSTANT_String</td>
 *  </tr>
 * </table>
 *
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantValueAttribute extends AbstractAttributeInfo {
    private final int constantValueIndex;

    public ConstantValueAttribute(int attributeNameIndex, int attributeLength, int constantValueIndex) {
        super(attributeNameIndex, attributeLength);
        this.constantValueIndex = constantValueIndex;
    }

    public int getConstantValueIndex() {
        return constantValueIndex;
    }
}
