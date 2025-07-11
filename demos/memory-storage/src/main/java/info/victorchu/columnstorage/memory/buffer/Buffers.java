package info.victorchu.columnstorage.memory.buffer;

import info.victorchu.columnstorage.memory.segment.SizeCalculator;
import info.victorchu.columnstorage.utils.SizeOf;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

/**
 * buffer工具类
 */
public class Buffers
{
    public static Buffer wrappedBuffer(byte[] array)
    {
        if (array.length == 0) {
            return Buffer.EMPTY_BUFFER;
        }
        return new Buffer(array);
    }

    public static Buffer wrappedBuffer(byte[] array, int offset, int length)
    {
        if (length == 0) {
            return Buffer.EMPTY_BUFFER;
        }
        return new Buffer(array, offset, length);
    }

    public static Buffer copiedBuffer(String string, Charset charset)
    {
        requireNonNull(string, "string is null");
        requireNonNull(charset, "charset is null");

        return wrappedBuffer(string.getBytes(charset));
    }

    public static Buffer utf8Buffer(String string)
    {
        return copiedBuffer(string, UTF_8);
    }

    public static Buffer allocate(int capacity)
    {
        if (capacity == 0) {
            return Buffer.EMPTY_BUFFER;
        }
        if (capacity > SizeCalculator.MAX_ARRAY_SIZE) {
            throw new BufferTooLargeException(String.format("Cannot allocate buffer larger than %s bytes", SizeCalculator.MAX_ARRAY_SIZE * SizeOf.SIZE_OF_BYTE));
        }
        return new Buffer(new byte[capacity]);
    }
}
