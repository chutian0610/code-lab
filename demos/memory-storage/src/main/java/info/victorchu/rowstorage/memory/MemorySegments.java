package info.victorchu.rowstorage.memory;

import java.nio.ByteBuffer;

import static info.victorchu.rowstorage.utils.Preconditions.checkArgument;

public class MemorySegments
{
    /**
     * Creates a new memory segment that targets the given heap memory region.
     *
     * <p>This method should be used to turn short lived byte arrays into memory segments.
     *
     * @param buffer The heap memory region.
     * @return A new memory segment that targets the given heap memory region.
     */
    public static MemorySegment wrap(byte[] buffer) {
        return new MemorySegment(buffer);
    }


    /**
     * Wraps the four bytes representing the given number with a {@link MemorySegment}.
     *
     * @see ByteBuffer#putInt(int)
     */
    public static MemorySegment wrapInt(int value) {
        return wrap(ByteBuffer.allocate(Integer.BYTES).putInt(value).array());
    }

    /**
     * Copies the given heap memory region and creates a new memory segment wrapping it.
     *
     * @param bytes The heap memory region.
     * @param start starting position, inclusive
     * @param end end position, exclusive
     * @return A new memory segment that targets a copy of the given heap memory region.
     * @throws IllegalArgumentException if start > end or end > bytes.length
     */
    public static MemorySegment wrapCopy(byte[] bytes, int start, int end)
            throws IllegalArgumentException {
        checkArgument(end >= start);
        checkArgument(end <= bytes.length);
        MemorySegment copy = allocate(end - start);
        copy.put(0, bytes, start, copy.size());
        return copy;
    }


    /**
     * Allocates some memory and creates a new memory segment that represents that memory.
     *
     * @param size The size of the memory segment to allocate.
     * @return A new memory segment, backed by unpooled heap memory.
     */
    public static MemorySegment allocate(int size) {
        return new MemorySegment(new byte[size]);
    }

    /**
     * Allocates some unpooled off-heap memory and creates a new memory segment that represents that
     * memory.
     *
     * @param size The size of the off-heap memory segment to allocate.
     * @return A new memory segment, backed by unpooled off-heap memory.
     */
    public static MemorySegment allocateOffHeapMemory(int size) {
        ByteBuffer memory = allocateDirectMemory(size);
        return new MemorySegment(memory);
    }

    private static ByteBuffer allocateDirectMemory(int size) {
        return ByteBuffer.allocateDirect(size);
    }

    public static MemorySegment allocateOffHeapUnsafeMemory(
            int size, Runnable customCleanupAction) {
        long address = MemoryUtils.allocateUnsafe(size);
        ByteBuffer offHeapBuffer = MemoryUtils.wrapUnsafeMemoryWithByteBuffer(address, size);
        Runnable cleaner = MemoryUtils.createMemoryCleaner(address, customCleanupAction);
        return new MemorySegment(offHeapBuffer, false, cleaner);
    }

    /**
     * Creates a memory segment that wraps the off-heap memory backing the given ByteBuffer. Note
     * that the ByteBuffer needs to be a <i>direct ByteBuffer</i>.
     *
     * <p>This method is intended to be used for components which pool memory and create memory
     * segments around long-lived memory regions.
     *
     * @param memory The byte buffer with the off-heap memory to be represented by the memory
     *     segment.
     * @return A new memory segment representing the given off-heap memory.
     */
    public static MemorySegment wrapOffHeapMemory(ByteBuffer memory) {
        return new MemorySegment(memory);
    }

}
