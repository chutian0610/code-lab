package info.victorchu.jdk.lab.usage.type.util;


/**
 * PrimitiveTypeResolver.
 * 由于Class类只有isPrimitive方法，并不能感知到具体基类的类型。所以有了这个工具类。
 */
public class PrimitiveTypeResolver {

    public enum PrimitiveType {
        Boolean,
        Character,
        Byte,
        Short,
        Integer,
        Long,
        Float,
        Double,
        Void
    }


    public static PrimitiveType resolve(Class clazz) {
        if (!clazz.isPrimitive()) {
            return null;
        }
        // primitive class
        String name = clazz.getName();
        switch (name) {
            case "long":
                return PrimitiveType.Long;
            case "int":
                return PrimitiveType.Integer;
            case "float":
                return PrimitiveType.Float;
            case "double":
                return PrimitiveType.Double;
            case "char":
                return PrimitiveType.Character;
            case "boolean":
                return PrimitiveType.Boolean;
            case "short":
                return PrimitiveType.Short;
            case "byte":
                return PrimitiveType.Byte;
            case "void":
                return PrimitiveType.Void;
            default:
                throw new IllegalArgumentException("None Supported Primitive Class!");
        }

    }

}