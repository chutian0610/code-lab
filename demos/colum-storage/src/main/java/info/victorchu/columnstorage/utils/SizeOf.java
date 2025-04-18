package info.victorchu.columnstorage.utils;

import org.openjdk.jol.info.ClassData;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.FieldData;
import org.openjdk.jol.util.MathUtil;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToLongFunction;

import static java.lang.Math.toIntExact;
import static sun.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_LONG_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;
import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;

public final class SizeOf
{

    public static final byte SIZE_OF_BYTE = 1;
    public static final byte SIZE_OF_SHORT = 2;
    public static final byte SIZE_OF_INT = 4;
    public static final byte SIZE_OF_LONG = 8;
    public static final byte SIZE_OF_FLOAT = 4;
    public static final byte SIZE_OF_DOUBLE = 8;

    public static final int BOOLEAN_INSTANCE_SIZE = instanceSize(Boolean.class);
    public static final int BYTE_INSTANCE_SIZE = instanceSize(Byte.class);
    public static final int SHORT_INSTANCE_SIZE = instanceSize(Short.class);
    public static final int CHARACTER_INSTANCE_SIZE = instanceSize(Character.class);
    public static final int INTEGER_INSTANCE_SIZE = instanceSize(Integer.class);
    public static final int LONG_INSTANCE_SIZE = instanceSize(Long.class);
    public static final int FLOAT_INSTANCE_SIZE = instanceSize(Float.class);
    public static final int DOUBLE_INSTANCE_SIZE = instanceSize(Double.class);

    public static final int OPTIONAL_INSTANCE_SIZE = instanceSize(Optional.class);
    public static final int OPTIONAL_INT_INSTANCE_SIZE = instanceSize(OptionalInt.class);
    public static final int OPTIONAL_LONG_INSTANCE_SIZE = instanceSize(OptionalLong.class);
    public static final int OPTIONAL_DOUBLE_INSTANCE_SIZE = instanceSize(OptionalDouble.class);

    public static final int STRING_INSTANCE_SIZE = instanceSize(String.class);

    private static final int SIMPLE_ENTRY_INSTANCE_SIZE = instanceSize(AbstractMap.SimpleEntry.class);

    private SizeOf()
    {
    }

    public static long sizeOf(boolean[] array)
    {
        return (array == null) ? 0 : sizeOfBooleanArray(array.length);
    }

    public static long sizeOf(byte[] array)
    {
        return (array == null) ? 0 : sizeOfByteArray(array.length);
    }

    public static long sizeOf(short[] array)
    {
        return (array == null) ? 0 : sizeOfShortArray(array.length);
    }

    public static long sizeOf(char[] array)
    {
        return (array == null) ? 0 : sizeOfCharArray(array.length);
    }

    public static long sizeOf(int[] array)
    {
        return (array == null) ? 0 : sizeOfIntArray(array.length);
    }

    public static long sizeOf(long[] array)
    {
        return (array == null) ? 0 : sizeOfLongArray(array.length);
    }

    public static long sizeOf(float[] array)
    {
        return (array == null) ? 0 : sizeOfFloatArray(array.length);
    }

    public static long sizeOf(double[] array)
    {
        return (array == null) ? 0 : sizeOfDoubleArray(array.length);
    }

    public static long sizeOf(Object[] array)
    {
        return (array == null) ? 0 : sizeOfObjectArray(array.length);
    }

    public static long sizeOf(Boolean value)
    {
        return value == null ? 0 : BOOLEAN_INSTANCE_SIZE;
    }

    public static long sizeOf(Byte value)
    {
        return value == null ? 0 : BYTE_INSTANCE_SIZE;
    }

    public static long sizeOf(Short value)
    {
        return value == null ? 0 : SHORT_INSTANCE_SIZE;
    }

    public static long sizeOf(Character value)
    {
        return value == null ? 0 : CHARACTER_INSTANCE_SIZE;
    }

    public static long sizeOf(Integer value)
    {
        return value == null ? 0 : INTEGER_INSTANCE_SIZE;
    }

    public static long sizeOf(Long value)
    {
        return value == null ? 0 : LONG_INSTANCE_SIZE;
    }

    public static long sizeOf(Float value)
    {
        return value == null ? 0 : FLOAT_INSTANCE_SIZE;
    }

    public static long sizeOf(Double value)
    {
        return value == null ? 0 : DOUBLE_INSTANCE_SIZE;
    }

    public static <T> long sizeOf(Optional<T> optional, ToLongFunction<T> valueSize)
    {
        return optional != null && optional.isPresent() ? OPTIONAL_INSTANCE_SIZE + valueSize.applyAsLong(optional.get()) : 0;
    }

    public static long sizeOf(OptionalInt optional)
    {
        return optional != null && optional.isPresent() ? OPTIONAL_INT_INSTANCE_SIZE : 0;
    }

    public static long sizeOf(OptionalLong optional)
    {
        return optional != null && optional.isPresent() ? OPTIONAL_LONG_INSTANCE_SIZE : 0;
    }

    public static long sizeOf(OptionalDouble optional)
    {
        return optional != null && optional.isPresent() ? OPTIONAL_DOUBLE_INSTANCE_SIZE : 0;
    }

    public static long sizeOfBooleanArray(int length)
    {
        return ARRAY_BOOLEAN_BASE_OFFSET + (((long) ARRAY_BOOLEAN_INDEX_SCALE) * length);
    }

    public static long sizeOfByteArray(int length)
    {
        return ARRAY_BYTE_BASE_OFFSET + (((long) ARRAY_BYTE_INDEX_SCALE) * length);
    }

    public static long sizeOfShortArray(int length)
    {
        return ARRAY_SHORT_BASE_OFFSET + (((long) ARRAY_SHORT_INDEX_SCALE) * length);
    }

    public static long sizeOfCharArray(int length)
    {
        return ARRAY_CHAR_BASE_OFFSET + (((long) ARRAY_CHAR_INDEX_SCALE) * length);
    }

    public static long sizeOfIntArray(int length)
    {
        return ARRAY_INT_BASE_OFFSET + (((long) ARRAY_INT_INDEX_SCALE) * length);
    }

    public static long sizeOfLongArray(int length)
    {
        return ARRAY_LONG_BASE_OFFSET + (((long) ARRAY_LONG_INDEX_SCALE) * length);
    }

    public static long sizeOfFloatArray(int length)
    {
        return ARRAY_FLOAT_BASE_OFFSET + (((long) ARRAY_FLOAT_INDEX_SCALE) * length);
    }

    public static long sizeOfDoubleArray(int length)
    {
        return ARRAY_DOUBLE_BASE_OFFSET + (((long) ARRAY_DOUBLE_INDEX_SCALE) * length);
    }

    public static long sizeOfObjectArray(int length)
    {
        return ARRAY_OBJECT_BASE_OFFSET + (((long) ARRAY_OBJECT_INDEX_SCALE) * length);
    }

    public static long estimatedSizeOf(String string)
    {
        return (string == null) ? 0 : (STRING_INSTANCE_SIZE + string.length() * Character.BYTES);
    }

    public static <T> long estimatedSizeOf(List<T> list, ToLongFunction<T> valueSize)
    {
        if (list == null) {
            return 0;
        }

        long result = sizeOfObjectArray(list.size());
        for (T value : list) {
            result += valueSize.applyAsLong(value);
        }
        return result;
    }

    public static <T> long estimatedSizeOf(Queue<T> queue, ToLongFunction<T> valueSize)
    {
        if (queue == null) {
            return 0;
        }

        long result = sizeOfObjectArray(queue.size());
        for (T value : queue) {
            result += valueSize.applyAsLong(value);
        }
        return result;
    }

    public static <T> long estimatedSizeOf(Set<T> set, ToLongFunction<T> valueSize)
    {
        if (set == null) {
            return 0;
        }

        long result = sizeOfObjectArray(set.size());
        for (T value : set) {
            result += SIMPLE_ENTRY_INSTANCE_SIZE + valueSize.applyAsLong(value);
        }
        return result;
    }

    public static <K, V> long estimatedSizeOf(Map<K, V> map, ToLongFunction<K> keySize, ToLongFunction<V> valueSize)
    {
        if (map == null) {
            return 0;
        }

        long result = sizeOfObjectArray(map.size());
        for (Entry<K, V> entry : map.entrySet()) {
            result += SIMPLE_ENTRY_INSTANCE_SIZE +
                    keySize.applyAsLong(entry.getKey()) +
                    valueSize.applyAsLong(entry.getValue());
        }
        return result;
    }

    public static <K, V> long estimatedSizeOf(Map<K, V> map, long keySize, long valueSize)
    {
        if (map == null) {
            return 0;
        }

        long result = sizeOfObjectArray(map.size());
        result += map.size() * (SIMPLE_ENTRY_INSTANCE_SIZE + keySize + valueSize);
        return result;
    }

    public static int instanceSize(Class<?> clazz)
    {
        try {
            return toIntExact(ClassLayout.parseClass(clazz).instanceSize());
        }
        catch (RuntimeException e) {
            VirtualMachine vm = VM.current();
            ClassData classData = ClassData.parseClass(clazz);
            long instanceSize = vm.objectHeaderSize();
            for (FieldData field : classData.fields()) {
                instanceSize += vm.sizeOfField(field.typeClass());
            }
            instanceSize = MathUtil.align(instanceSize, vm.objectAlignment());
            return toIntExact(instanceSize);
        }
    }
}