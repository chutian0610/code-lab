package info.victorchu.columnstorage.segment;

import static info.victorchu.columnstorage.utils.SizeOf.instanceSize;

public class SegmentStatus
        implements RetainedSizeAware
{
    public static final int INSTANCE_SIZE = instanceSize(SegmentStatus.class);
    private int currentSize;
    private long memUsedSize;

    public void updateMemUsedSize(long retainedSize)
    {
        this.memUsedSize = retainedSize;
    }

    public void addBytes(int bytes)
    {
        this.currentSize += bytes;
    }

    public int getCurrentSize()
    {
        return currentSize;
    }

    public long getMemUsedSize()
    {
        return memUsedSize;
    }

    @Override
    public long getRetainedSize()
    {
        return INSTANCE_SIZE;
    }
}
