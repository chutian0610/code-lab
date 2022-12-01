package info.victorchu.commontool.tuple.simple;

/**
 * @author victorchu
 * @date 2022/8/10 23:24
 */
public interface Tuple {

    /**
     * 获取元组的类型描述
     * @return
     */
    public TupleType getType();

    /**
     * 获取元组的长度
     * @return
     */
    public int size();

    /**
     * 获取第i个元素的值
     * @param i
     * @param <T>
     * @return
     */
    public <T> T getNthValue(int i);
}
