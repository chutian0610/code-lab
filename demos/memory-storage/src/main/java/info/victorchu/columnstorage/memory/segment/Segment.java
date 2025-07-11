package info.victorchu.columnstorage.memory.segment;

/**
 * segment 用于存储一列相同类型的数据
 */
public interface Segment
{
    int getPosition();

    int getCapacity();
}
