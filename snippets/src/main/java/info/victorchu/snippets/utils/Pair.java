package info.victorchu.snippets.utils;

/**
 * @author victorchu
 */
public class Pair<T, U>
{

    public T getLeft()
    {
        return left;
    }

    public U getRight()
    {
        return right;
    }

    private final T left;
    private final U right;

    private Pair(T first, U second)
    {
        this.left = first;
        this.right = second;
    }

    public static <T, U> Pair<T, U> of(T first, U second)
    {
        return new Pair<T, U>(first, second);
    }

    @Override
    public String toString()
    {
        return "(" + left + ", " + right + ")";
    }
}
