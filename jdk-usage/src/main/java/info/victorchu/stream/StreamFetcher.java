package info.victorchu.stream;

import info.victorchu.uitl.PrimitiveTypeResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream 的获取方式
 */
public class StreamFetcher {

    public static <T> Stream<T> fromCollection(Collection<T> collection,boolean parallel) {
        return (collection ==null || collection.isEmpty())?
                Stream.empty() : parallel ? collection.parallelStream() :collection.stream();
    }

    public static <T> Stream<T> fromArray(T[] array,int startInclusive, int endExclusive,boolean parallel) {
        return (array ==null || array.length == 0)?
                Stream.empty() : parallel? Arrays.stream(array).parallel():Arrays.stream(array);
    }
}
