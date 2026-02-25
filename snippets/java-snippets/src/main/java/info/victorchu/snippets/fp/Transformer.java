package info.victorchu.snippets.fp;

import java.util.function.BiFunction;

@FunctionalInterface
public interface Transformer<S, C> {

    /**
     * transform 过程抽象
     *
     * @param source  转化的原始对象类型
     * @param context 转化的上下文
     * @return
     */
    Transformed<S, C> apply(S source, C context);


    /**
     * transform & get Reply
     *
     * @param source
     * @param context
     * @return
     */
    default Reply<S, C> transform(S source, C context) {
        return apply(source, context).getReply();
    }
}