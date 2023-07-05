package info.victorchu.jvmtool.clazz;

import info.victorchu.jvmtool.clazz.constant.ConstantInvokeDynamicInfo;
import info.victorchu.jvmtool.clazz.deserializer.ClassFileReader;
import info.victorchu.jvmtool.clazz.exception.ConstantPoolException;
import info.victorchu.jvmtool.clazz.constant.ConstantClassInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantDoubleInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantFieldRefInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantFloatInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantIntegerInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantInterfaceMethodRefInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantLongInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantMethodHandleInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantMethodRefInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantMethodTypeInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantNameAndTypeInfo;
import info.victorchu.jvmtool.clazz.constant.AbstractConstantPoolInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantStringInfo;
import info.victorchu.jvmtool.clazz.constant.ConstantUtf8Info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * constant pool table of class file，contains subtype as followed.
 *
 * <table border="1">
 *     <caption>constant pool</caption>
 *     <tr>
 *         <th>Constant Type</th><th>Value</th>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Class</td><td>7</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Fieldref</td><td>9</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Methodref</td><td>10</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_InterfaceMethodref</td><td>11</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_String</td><td>8</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Integer</td><td>3</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Float</td><td>4</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Long</td><td>5</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Double</td><td>6</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_NameAndType</td><td>12</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_Utf8</td><td>1</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_MethodHandle</td><td>15</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_MethodType</td><td>16</td>
 *     </tr>
 *     <tr>
 *         <td>CONSTANT_InvokeDynamic</td><td>18</td>
 *     </tr>
 * </table>
 * @see com.sun.tools.classfile.ConstantPool
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantPool {
    public static final int CONSTANT_UTF8 = 1;
    public static final int CONSTANT_INTEGER = 3;
    public static final int CONSTANT_FLOAT = 4;
    public static final int CONSTANT_LONG = 5;
    public static final int CONSTANT_DOUBLE = 6;
    public static final int CONSTANT_CLASS = 7;
    public static final int CONSTANT_STRING = 8;
    public static final int CONSTANT_FIELD_REF = 9;
    public static final int CONSTANT_METHOD_REF = 10;
    public static final int CONSTANT_INTERFACE_METHOD_REF = 11;
    public static final int CONSTANT_NAME_AND_TYPE = 12;
    public static final int CONSTANT_METHOD_HANDLE = 15;
    public static final int CONSTANT_METHOD_TYPE = 16;
    public static final int CONSTANT_INVOKE_DYNAMIC = 18;
    private final ArrayList<AbstractConstantPoolInfo> abstractConstantPoolInfoList;

    public ConstantPool(AbstractConstantPoolInfo[] pool) {
        this.abstractConstantPoolInfoList = new ArrayList<>(Arrays.asList(pool));
    }

    public ConstantPool(ClassFileReader cr) throws IOException {
        // constant pool array count
        int count = cr.readUnsignedShort();
        abstractConstantPoolInfoList = new ArrayList<>(count);
        if (count >0) {
            for (int i = 1; i < count; ++i) {
                // subtype index
                int tag = cr.readUnsignedByte();
                switch (tag) {
                    case ConstantPool.CONSTANT_UTF8:
                        abstractConstantPoolInfoList.add(new ConstantUtf8Info(tag, cr.readUTF()));
                        break;
                    case ConstantPool.CONSTANT_INTEGER:
                        abstractConstantPoolInfoList.add(new ConstantIntegerInfo(tag, cr.readInt()));
                        break;
                    case ConstantPool.CONSTANT_FLOAT:
                        abstractConstantPoolInfoList.add(new ConstantFloatInfo(tag, cr.readFloat()));
                        break;
                    case ConstantPool.CONSTANT_LONG:
                        abstractConstantPoolInfoList.add(new ConstantLongInfo(tag,cr.readLong()));
                        break;
                    case ConstantPool.CONSTANT_DOUBLE:
                        abstractConstantPoolInfoList.add(new ConstantDoubleInfo(tag,cr.readDouble()));
                        break;
                    case ConstantPool.CONSTANT_CLASS:
                        abstractConstantPoolInfoList.add(new ConstantClassInfo(tag,cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_STRING:
                        abstractConstantPoolInfoList.add(new ConstantStringInfo(tag,cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_FIELD_REF:
                        abstractConstantPoolInfoList.add(new ConstantFieldRefInfo(tag,cr.readUnsignedShort(),
                                cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_METHOD_REF:
                        abstractConstantPoolInfoList.add(new ConstantMethodRefInfo(tag,cr.readUnsignedShort(),
                                cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_INTERFACE_METHOD_REF:
                        abstractConstantPoolInfoList.add(new ConstantInterfaceMethodRefInfo(tag,cr.readUnsignedShort(),
                                cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_NAME_AND_TYPE:
                        abstractConstantPoolInfoList.add(new ConstantNameAndTypeInfo(tag,cr.readUnsignedShort(),
                                cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_METHOD_HANDLE:
                        abstractConstantPoolInfoList.add(new ConstantMethodHandleInfo(tag,cr.readUnsignedByte()
                                ,cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_METHOD_TYPE:
                        abstractConstantPoolInfoList.add(new ConstantMethodTypeInfo(tag,cr.readUnsignedShort()));
                        break;
                    case ConstantPool.CONSTANT_INVOKE_DYNAMIC:
                        abstractConstantPoolInfoList.add(new ConstantInvokeDynamicInfo(tag,cr.readUnsignedShort()
                                ,cr.readUnsignedShort()));
                        break;
                    default:
                        throw new ConstantPoolException(
                                "Invalid constant pool tag, this kind tag{"+ tag +"} is not supported currently!");


                }
            }
        }
    }

    public AbstractConstantPoolInfo get(int i) {
        return abstractConstantPoolInfoList.get(i);
    }

    public ConstantUtf8Info getUtf8Info(int i){
        AbstractConstantPoolInfo abstractConstantPoolInfo = abstractConstantPoolInfoList.get(i);
        if(abstractConstantPoolInfo.getTag() == CONSTANT_UTF8){
            return (ConstantUtf8Info) abstractConstantPoolInfo;
        }else {
            throw new ConstantPoolException("unexpected constant at #" + i + " -- expected tag 1, found "+ abstractConstantPoolInfo.getTag());
        }
    }

    public int size() {
        return abstractConstantPoolInfoList.size();
    }
}
