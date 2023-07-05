package info.victorchu.jvmtool.clazz;

import info.victorchu.jvmtool.clazz.deserializer.ClassFileReader;
import info.victorchu.jvmtool.clazz.exception.ConstantPoolException;
import info.victorchu.jvmtool.clazz.exception.DescriptorException;

import java.io.IOException;

/**
 * 描述符.
 *
 * 描述符是表示字段或方法类型的字符串。描述符使用修改后的 UTF-8 字符串。
 *
 * <pre>
 * ```字段描述符
 * FieldDescriptor:
 *   FieldType
 * // 字段类型
 * FieldType:
 *   BaseType | ObjectType | ArrayType
 * // 值类型
 * BaseType:
 *   'B' | 'C' | 'D' | 'F' | 'I' | 'J' | 'S' | 'Z'
 * // 对象类型
 * ObjectType:
 *   'L' ClassName ';'
 * // 数组类型
 * ArrayType:
 *   '[' ComponentType
 * // 元素类型
 * ComponentType:
 *   FieldType
 * ```
 *
 * ```方法描述图
 * MethodDescriptor:
 *   ( ParameterDescriptor* ) ReturnDescriptor
 * ParameterDescriptor:
 *   FieldType
 * ReturnDescriptor:
 *   FieldType | VoidDescriptor
 * VoidDescriptor:
 *   'V'
 * ```
 * </pre>
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */
public class Descriptor {

    /**
     *
     */
    public static class InvalidDescriptorException extends DescriptorException {
        private static final long serialVersionUID = 1L;
        InvalidDescriptorException(String desc) {
            this.desc = desc;
            this.index = -1;
        }

        InvalidDescriptorException(String desc, int index) {
            this.desc = desc;
            this.index = index;
        }

        @Override
        public String getMessage() {
            // i18n
            if (index == -1)
                return "invalid descriptor \"" + desc + "\"";
            else
                return "descriptor is invalid at offset " + index + " in \"" + desc + "\"";
        }

        public final String desc;
        public final int index;

    }

    public Descriptor(ClassFileReader cr) throws IOException {
        this(cr.readUnsignedShort());
    }

    public Descriptor(int index) {
        this.index = index;

    }

    public String getValue(ConstantPool constant_pool) throws ConstantPoolException {
        return constant_pool.getUtf8Info(index).getUtf8();
    }

    public int getParameterCount(ConstantPool constant_pool)
            throws ConstantPoolException, InvalidDescriptorException {
        String desc = getValue(constant_pool);
        int end = desc.indexOf(")");
        if (end == -1)
            throw new InvalidDescriptorException(desc);
        parse(desc, 0, end + 1);
        return count;

    }

    public String getParameterTypes(ConstantPool constant_pool)
            throws ConstantPoolException, InvalidDescriptorException {
        String desc = getValue(constant_pool);
        int end = desc.indexOf(")");
        if (end == -1)
            throw new InvalidDescriptorException(desc);
        return parse(desc, 0, end + 1);
    }

    public String getReturnType(ConstantPool constant_pool)
            throws ConstantPoolException, InvalidDescriptorException {
        String desc = getValue(constant_pool);
        int end = desc.indexOf(")");
        if (end == -1)
            throw new InvalidDescriptorException(desc);
        return parse(desc, end + 1, desc.length());
    }

    public String getFieldType(ConstantPool constant_pool)
            throws ConstantPoolException, InvalidDescriptorException {
        String desc = getValue(constant_pool);
        return parse(desc, 0, desc.length());
    }

    private String parse(String desc, int start, int end)
            throws InvalidDescriptorException {
        int p = start;
        StringBuilder sb = new StringBuilder();
        int dims = 0;
        count = 0;

        while (p < end) {
            String type;
            char ch;
            switch (ch = desc.charAt(p++)) {
                case '(':
                    sb.append('(');
                    continue;

                case ')':
                    sb.append(')');
                    continue;

                case '[':
                    dims++;
                    continue;

                case 'B':
                    type = "byte";
                    break;

                case 'C':
                    type = "char";
                    break;

                case 'D':
                    type = "double";
                    break;

                case 'F':
                    type = "float";
                    break;

                case 'I':
                    type = "int";
                    break;

                case 'J':
                    type = "long";
                    break;

                case 'L':
                    int sep = desc.indexOf(';', p);
                    if (sep == -1)
                        throw new InvalidDescriptorException(desc, p - 1);
                    type = desc.substring(p, sep).replace('/', '.');
                    p = sep + 1;
                    break;

                case 'S':
                    type = "short";
                    break;

                case 'Z':
                    type = "boolean";
                    break;

                case 'V':
                    type = "void";
                    break;

                default:
                    throw new InvalidDescriptorException(desc, p - 1);
            }

            if (sb.length() > 1 && sb.charAt(0) == '(')
                sb.append(", ");
            sb.append(type);
            for ( ; dims > 0; dims-- )
                sb.append("[]");

            count++;
        }
        return sb.toString();
    }

    public final int index;
    private int count;
}
