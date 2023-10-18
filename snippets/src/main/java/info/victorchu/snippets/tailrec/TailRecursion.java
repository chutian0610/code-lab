package info.victorchu.snippets.tailrec;

import java.util.stream.Stream;

/**
 * 尾递归
 * @param <T> 迭代参数类型
 * @param <R> 迭代结果
 */
@FunctionalInterface
public interface TailRecursion<T,R> {
    /**
     * 函数实际逻辑。
     * func(T,R) -> func(T',R')
     * @return func
     */
    TailRecursion<T,R> apply();

    default boolean isFinished(){
        return false;
    }
    default R getResult() {
        throw new IllegalStateException("Recursion not End");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    default R invoke() {
        return Stream.iterate(this, TailRecursion::apply)
                .filter(TailRecursion::isFinished)
                .findFirst().get()
                .getResult();
    }

    static < T,R> TailRecursion < T, R> done (final R value) {
        return new TailRecursion <T,R>() {
            @Override
            public boolean isFinished () { return true ; }
            @Override
            public R getResult () { return value ; }
            @Override
            public TailRecursion<T,R> apply () {
                throw new IllegalStateException ( "Recursion Ended" );
            }
        };
    }
}
