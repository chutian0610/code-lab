package info.victorchu.columnstorage.memory.buffer;

public class BufferTooLargeException
        extends IllegalArgumentException
{
    public BufferTooLargeException(String message)
    {
        super(message);
    }
}
