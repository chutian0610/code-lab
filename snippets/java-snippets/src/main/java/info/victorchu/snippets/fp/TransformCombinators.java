package info.victorchu.snippets.fp;

import java.util.List;
import java.util.function.BiFunction;

public class TransformCombinators {

    static class Tuple2<T0, T1> {

        public Tuple2(T0 f0, T1 f1) {
            this.f0 = f0;
            this.f1 = f1;
        }

        /**
         * Field 0 of the tuple.
         */
        public T0 f0;
        /**
         * Field 1 of the tuple.
         */
        public T1 f1;

        public static <T0, T1> Tuple2<T0, T1> of(T0 f0, T1 f1){
            return new Tuple2<>(f0, f1);
        }
    }

    public static <S, C> Transformer<S, C> retn(S x) {
        return (input,context) ->
                Transformed.unchanged(
                        Reply.ok(
                                x,
                                context,
                                Message.of("return "+ x))
                );
    }

    public static <S,C> Transformer<S,C> bind(Transformer<S,C> p, BiFunction<S, C, Transformer<S, C>> f) {
        return (input, context) -> {
            final Transformed<S,C> transformed1 = p.apply(input, context);
            if (transformed1.isChanged()) {
                return Transformed.changed(
                        transformed1.getReply().match(
                                ok1 -> {
                                    final Transformed<S, C> cons2 = f.apply(ok1.result, ok1.context).apply(ok1.result, ok1.context);
                                    return cons2.getReply();
                                },
                                error -> error.cast()
                        )
                );
            } else {
                return transformed1.getReply().match(
                        ok1 -> {
                            final Transformed<S, C> cons2 = f.apply(ok1.result, ok1.context).apply(ok1.result, ok1.context);
                            if (cons2.isChanged()) {
                                return cons2;
                            } else {
                                return cons2.getReply().match(
                                        ok2 -> Transformed.mergeOk(ok2.result, ok2.context, ok1.msg, ok2.msg),
                                        error -> Transformed.mergeError(error.context, ok1.msg, error.msg)
                                );
                            }
                        },
                        error -> Transformed.unchanged(error.cast())
                );
            }
        };
    }
    public static <S,C> Transformer<S,C> then(Transformer<S,C> p, Transformer<S,C> q) {
        return (input, context) -> {
            final Transformed<S, C> transformed = p.apply(input,context);
            if (transformed.isChanged()) {
                return Transformed.changed(
                        transformed.getReply().match(
                                ok1 -> {
                                    final Transformed<S, C> cons2 = q.apply(ok1.result, ok1.context);
                                    return cons2.getReply();
                                },
                                error -> error.cast()
                        )
                );
            } else {
                return transformed.getReply().match(
                        ok1 -> {
                            final Transformed<S, C>  cons2 = q.apply(ok1.result, ok1.context);
                            if (cons2.isChanged()) {
                                return cons2;
                            } else {
                                return cons2.getReply().match(
                                        ok2 -> Transformed.mergeOk(ok2.result, ok2.context, ok1.msg, ok2.msg),
                                        error2 -> Transformed.mergeError(error2.context,ok1.msg, error2.msg)
                                );
                            }
                        },
                        error -> transformed
                );
            }
        };
    }

    public static <S,C> Transformer<S,C> list(List<? extends Transformer<S,C>> transformers) {
        Transformer<S,C>[] tarray = new Transformer[transformers.size()];
        for(int i=0;i<transformers.size();i++){
            tarray[i] = transformers.get(i);
        }
        return sequence(tarray);
    }
    public static <S,C> Transformer<S,C> sequence(Transformer<S,C>... transformers) {
        return (input, context) -> {
            S s = input;
            C c = context;
            Transformed<S, C>  transformed = Transformed.unchanged(Reply.ok(input,context,Message.of()));
            for(Transformer<S,C> transformer:transformers){
                Transformed<S, C> subTransformed = transformer.apply(s,c);
                if(!subTransformed.isOK()){
                    return subTransformed;
                }
                Tuple2<S, C> tuple2 = subTransformed.getReply().match(
                        ok -> Tuple2.of(ok.getResult(), ok.getContext()),
                        error ->  Tuple2.of(input, context)
                );
                s = tuple2.f0;
                c = tuple2.f1;
                transformed = Transformed.of(
                        subTransformed.isChanged()? subTransformed.isChanged(): transformed.isChanged(),
                        subTransformed.getReply());
            }
            return transformed;
        };
    }

}
