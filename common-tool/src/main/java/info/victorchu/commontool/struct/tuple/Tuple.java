package info.victorchu.commontool.struct.tuple;

/**
 * 元组接口.
 * @author victorchu
 * @date 2022/8/10 23:24
 */
public interface Tuple {

    /**
     * 获取元组的类型描述
     * @return 元组的类型描述
     */
    public TupleType getType();

    /**
     * 获取元组的长度
     * @return 元组的长度
     */
    public int size();

    /**
     * 获取第i个元素的值
     * @param i 元素索引
     * @param <T> 元素类型
     * @return 元素值
     */
    public <T> T getNthValue(int i);
}
