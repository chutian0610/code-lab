package info.victorchu.snippets.collection;

import com.google.common.base.Preconditions;

import java.util.Comparator;

/**
 * @author victorchu
 */
public class Ranges<T extends Comparable<T>>
{
    /**
     * 下限,null 表示无下限
     */
    private final T min;

    /**
     * 上限,null 表示无上限
     */
    private final T max;

    private final Comparator<T> comparator;
    public Ranges(T min, T max, Comparator<T> comparator){
        Preconditions.checkArgument(comparator !=null);
        if(min != null && max != null) {
            Preconditions.checkArgument(comparator.compare(min, max) < 0);
        }
        this.min = min;
        this.max = max;
        this.comparator = comparator;
    }

    public void addSingle(T value){
        Preconditions.checkArgument( value!= null);
        addSection(value,value);
    }

    public void addSection(T from,T to){
        Preconditions.checkArgument(from != null && to != null);
        Preconditions.checkArgument(comparator.compare(from,to)<=0);


    }


}
