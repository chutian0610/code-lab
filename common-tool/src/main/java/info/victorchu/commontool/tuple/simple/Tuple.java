package info.victorchu.commontool.tuple.simple;

/**
 * @author victorchu
 * @date 2022/8/10 23:24
 */
public interface Tuple {

    public TupleType getType();
    public int size();
    public <T> T getNthValue(int i);
}
