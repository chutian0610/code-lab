package info.victorchu.tool.jvm.charlene.clazz.attribute;


import info.victorchu.tool.jvm.charlene.clazz.ConstantPool;
import info.victorchu.tool.jvm.charlene.clazz.deserializer.ClassFileReader;

import java.io.IOException;

/**
 * 代表了jvm class 文件中的 attribute.
 *
 * <p>attribute 的通用结构如下:</p>
 * <pre>
 * attribute_info {
 *     u2 attributeNameIndex; // 属性名,指向常量池中某个CONSTANT_Utf8_info的属性名
 *     u4 attribute_length; // 属性长度
 *     u1 info[attribute_length];
 * }
 * </pre>
 *
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public abstract class AbstractAttributeInfo {

    public final int attributeNameIndex;
    public final int attributeLength;

    public AbstractAttributeInfo(int attributeNameIndex, int attributeLength) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    public int getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public int getAttributeLength() {
        return attributeLength;
    }
    // -------------------------------------------------------------------------------------------------------------------
    public static final String AnnotationDefault        = "AnnotationDefault";
    public static final String BootstrapMethods         = "BootstrapMethods";
    public static final String CharacterRangeTable      = "CharacterRangeTable";
    public static final String Code                     = "Code";
    public static final String ConstantValue            = "ConstantValue";
    public static final String CompilationID            = "CompilationID";
    public static final String Deprecated               = "Deprecated";
    public static final String EnclosingMethod          = "EnclosingMethod";
    public static final String Exceptions               = "Exceptions";
    public static final String InnerClasses             = "InnerClasses";
    public static final String LineNumberTable          = "LineNumberTable";
    public static final String LocalVariableTable       = "LocalVariableTable";
    public static final String LocalVariableTypeTable   = "LocalVariableTypeTable";
    public static final String MethodParameters         = "MethodParameters";
    public static final String RuntimeVisibleAnnotations = "RuntimeVisibleAnnotations";
    public static final String RuntimeInvisibleAnnotations = "RuntimeInvisibleAnnotations";
    public static final String RuntimeVisibleParameterAnnotations = "RuntimeVisibleParameterAnnotations";
    public static final String RuntimeInvisibleParameterAnnotations = "RuntimeInvisibleParameterAnnotations";
    public static final String RuntimeVisibleTypeAnnotations = "RuntimeVisibleTypeAnnotations";
    public static final String RuntimeInvisibleTypeAnnotations = "RuntimeInvisibleTypeAnnotations";
    public static final String Signature                = "Signature";
    public static final String SourceDebugExtension     = "SourceDebugExtension";
    public static final String SourceFile               = "SourceFile";
    public static final String SourceID                 = "SourceID";
    public static final String StackMap                 = "StackMap";
    public static final String StackMapTable            = "StackMapTable";
    public static final String Synthetic                = "Synthetic";

    public static AbstractAttributeInfo read(ClassFileReader cr,ConstantPool constantPool) throws IOException {
        int attributeNameIndex = cr.readUnsignedShort();
        int attributeLength = cr.readInt();
        String attributeName = constantPool.getUtf8Info(attributeNameIndex).getUtf8();
        switch (attributeName){
            case AnnotationDefault:
                break;
            case BootstrapMethods:
                break;
            case CharacterRangeTable:
                break;
            case Code:
                break;
            case ConstantValue:
                break;
            case CompilationID:
                break;
            case Deprecated:
                break;
            case EnclosingMethod:
                break;
            case Exceptions:
                break;
            case InnerClasses:
                break;
            case LineNumberTable:
                break;
            case LocalVariableTable:
                break;
            case LocalVariableTypeTable:
                break;
            case MethodParameters:
                break;
            case RuntimeVisibleAnnotations:
                break;
            case RuntimeInvisibleAnnotations:
                break;
            case RuntimeVisibleParameterAnnotations:
                break;
            case RuntimeInvisibleParameterAnnotations:
                break;
            case RuntimeVisibleTypeAnnotations:
                break;
            case RuntimeInvisibleTypeAnnotations:
                break;
            case Signature:
                break;
            case SourceDebugExtension:
                break;
            case SourceFile:
                break;
            case SourceID:
                break;
            case StackMap:
                break;
            case StackMapTable:
                break;
            case Synthetic:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attributeName);
        }
        return null;
    }
}
