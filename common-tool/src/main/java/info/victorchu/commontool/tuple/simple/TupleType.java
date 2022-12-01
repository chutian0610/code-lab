package info.victorchu.commontool.tuple.simple;

/**
 * @author victorchu
 * @date 2022/8/10 23:25
 */
public interface TupleType {

    /**
     * 获取元组的长度
     * @return
     */
    int size();

    /**
     * 获取第N个元素类型
     * @param i
     * @return
     */
    Class<?> getNthType(int i);

    /**
     * Tuple are immutable objects.  Tuples should contain only immutable objects or
     * objects that won't be modified while part of a tuple.
     *
     * @param values
     * @return Tuple with the given values
     * @throws IllegalArgumentException if the wrong # of arguments or incompatible tuple values are provided
     */
    Tuple createTuple(Object... values);

    class DefaultFactory {
        public static TupleType create(final Class<?>... types) {
            return new TupleTypeImpl(types);
        }
    }

}
