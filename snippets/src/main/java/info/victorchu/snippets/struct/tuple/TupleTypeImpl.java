package info.victorchu.snippets.struct.tuple;

/**
 * @author victorchu

 */
public class TupleTypeImpl implements TupleType {

    final Class<?>[] types;

    TupleTypeImpl(Class<?>[] types) {
        this.types = (types != null ? types : new Class<?>[0]);
    }

    @Override
    public int size() {
        return types.length;
    }

    @Override
    public Class<?> getNthType(int i) {
        return types[i];
    }

    @Override
    public Tuple createTuple(Object... values) {
        if (values == null && types.length == 0) {
            throw new IllegalArgumentException(
                    "Expected "+types.length+" values, not Empty values");
        }
        if(values != null && values.length != types.length)
        {
            throw new IllegalArgumentException(
                    "Expected "+types.length+" values, not "+ values.length + " values");
        }

        if (values != null) {
            for (int i = 0; i < types.length; i++) {
                final Class<?> nthType = types[i];
                final Object nthValue = values[i];
                if (nthValue != null && ! nthType.isAssignableFrom(nthValue.getClass())) {
                    throw new IllegalArgumentException(
                            "Expected value #"+i+" ('"+
                                    nthValue+"') of new Tuple to be "+
                                    nthType+", not " +
                                    nthValue.getClass());
                }
            }
        }

        return new TupleImpl(this, values);
    }
}
