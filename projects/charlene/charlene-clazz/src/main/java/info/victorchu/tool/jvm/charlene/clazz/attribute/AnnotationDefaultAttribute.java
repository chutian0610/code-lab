package info.victorchu.tool.jvm.charlene.clazz.attribute;

/**
 * 代表了jvm class 文件中类型是 AnnotationDefault 的attribute.
 *
 * <p>AnnotationDefault 的结构如下:</p>
 * <pre>
 * AnnotationDefault_attribute {
 *     u2            attribute_name_index;
 *     u4            attribute_length;
 *     element_value default_value;
 * }
 * </pre>
 *
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class AnnotationDefaultAttribute extends AbstractAttributeInfo {
    public AnnotationDefaultAttribute(int attributeNameIndex, int attributeLength) {
        super(attributeNameIndex, attributeLength);
    }
}
