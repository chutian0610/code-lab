package info.victorchu.snippets.struct.tuple;

import java.util.Arrays;

/**
 * 元组的实现
 * @author victorchu

 */
public class TupleImpl implements Tuple {

    private final TupleType type;
    private final Object[] values;

    TupleImpl(TupleType type, Object[] values) {
        this.type = type;
        if (values == null || values.length == 0) {
            this.values = new Object[0];
        } else {
            this.values = new Object[values.length];
            System.arraycopy(values, 0, this.values, 0, values.length);
        }
    }

    /**
     *  {@inheritDoc}
     * @return
     */
    @Override
    public TupleType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int size() {
        return values.length;
    }

    /**
     * {@inheritDoc}
     * @param i 元素索引
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getNthValue(int i) {
        return (T) values[i];
    }

    /**
     * 重写equals方法.
     * 对Tuple中的每个元素判断是否equals
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object == null)  {
            return false;
        }
        if (this == object) {
            return true;
        }

        if (! (object instanceof Tuple))  {
            return false;
        }

        final Tuple other = (Tuple) object;
        if (other.size() != size())  {
            return false;
        }

        final int size = size();
        for (int i = 0; i < size; i++) {
            final Object thisNthValue = getNthValue(i);
            final Object otherNthValue = other.getNthValue(i);
            if ((thisNthValue == null && otherNthValue != null) ||
                    (thisNthValue != null && ! thisNthValue.equals(otherNthValue))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重写 hashCode 方法.
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 17;
        for (Object value : values) {
            if (value != null) {
                hash = hash * 37 + value.hashCode();
            }
        }
        return hash;
    }

    /**
     * 重写 toString 方法.
     * @return
     */
    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}