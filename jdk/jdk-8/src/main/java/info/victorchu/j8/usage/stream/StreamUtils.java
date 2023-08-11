package info.victorchu.j8.usage.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**工具类.
 * Stream 的获取方式
 */
public class StreamUtils {

    public static <T> Stream<T> fromCollection(Collection<T> collection,boolean parallel) {
        return (collection ==null || collection.isEmpty())?
                Stream.empty() : parallel ? collection.parallelStream() :collection.stream();
    }

    public static <T> Stream<T> fromArray(T[] array,int startInclusive, int endExclusive,boolean parallel) {
        return (array ==null || array.length == 0)?
                Stream.empty() : parallel? Arrays.stream(array).parallel():Arrays.stream(array);
    }

    /***************** 重载方法 ***********************/
    public static IntStream fromArray(int[] array,int startInclusive, int endExclusive,boolean parallel) {
        return (array ==null || array.length == 0)?
                IntStream.empty() : parallel? Arrays.stream(array).parallel():Arrays.stream(array);
    }

    public static LongStream fromArray(long[] array, int startInclusive, int endExclusive, boolean parallel) {
        return (array ==null || array.length == 0)?
                LongStream.empty() : parallel? Arrays.stream(array).parallel():Arrays.stream(array);
    }

    public static DoubleStream fromArray(double[] array, int startInclusive, int endExclusive, boolean parallel) {
        return (array ==null || array.length == 0)?
                DoubleStream.empty() : parallel? Arrays.stream(array).parallel():Arrays.stream(array);
    }
}
