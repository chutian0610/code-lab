package info.victorchu.columnstorage.buffer;

public class BufferTooLargeException
        extends IllegalArgumentException
{
    public BufferTooLargeException(String message)
    {
        super(message);
    }
}
