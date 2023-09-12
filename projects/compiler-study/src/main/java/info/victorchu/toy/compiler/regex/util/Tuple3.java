package info.victorchu.toy.compiler.regex.util;

/**
 * @author victorchu
 * @date 2023/9/12 21:16
 */
public class Tuple3<T1, T2, T3>
{
    private Tuple3(T1 t1, T2 t2, T3 t3)
    {
        _1 = t1;
        _2 = t2;
        _3 = t3;
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3)
    {
        return new Tuple3<>(t1, t2, t3);
    }

    public T1 getT1()
    {
        return _1;
    }

    public T2 getT2()
    {
        return _2;
    }

    public T3 getT3()
    {
        return _3;
    }

    private T1 _1;
    private T2 _2;
    private T3 _3;
}
