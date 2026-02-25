package info.victorchu.snippets.fp;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface Transformed<S, C> {

    static <S, C> Transformed<S, C> mergeOk(S source, C context, Message message1, Message message2) {
        return Transformed.unchanged(Reply.ok(source, context, message1.merge(message2)));
    }

    static <S, C> Transformed<S, C> mergeError(C context, Message message1, Message message2) {
        return Transformed.unchanged(Reply.error(context, message1.merge(message2)));
    }

    static <S, C> Transformed<S, C> changed(Reply<S, C> reply) {
        return new Changed<>(reply);
    }

    static <S, C> Transformed<S, C> unchanged(Reply<S, C> reply) {
        return new UnChanged<>(reply);
    }

    static <S, C> Transformed<S, C> of(boolean changed, Reply<S, C> reply) {
        return changed ?
                Transformed.changed(reply) :
                Transformed.unchanged(reply);
    }

    boolean isChanged();

    public abstract <U> U match(Function<Changed<S, C>, U> changed,
                                Function<Transformed.UnChanged<S, C>, U> unchanged);

    default boolean condition(Function<Transformed<S, C>,Boolean> function){
        return function.apply(this);
    }

    Reply<S, C> getReply();

    default boolean isOK(){
        return getReply().<Boolean>match(
                ok -> true,
                error ->false
        );
    }

    default <T> Transformed<T, C> cast() {
        return (Transformed<T, C>)this;
    }

    final class UnChanged<S, C> implements Transformed<S, C> {

        private final Reply<S, C> reply;

        UnChanged(Reply<S, C> reply) {
            this.reply = reply;
        }

        @Override
        public boolean isChanged() {
            return false;
        }

        @Override
        public <U> U match(Function<Changed<S, C>, U> changed, Function<UnChanged<S, C>, U> unchanged) {
            return unchanged.apply(this);
        }

        @Override
        public Reply<S, C> getReply() {
            return reply;
        }
    }

    class Changed<S, C> implements Transformed<S, C> {

        private final Reply<S, C> reply;

        Changed(Reply<S, C> supplier) {
            this.reply = supplier;
        }

        @Override
        public <U> U match(Function<Changed<S, C>, U> changed, Function<UnChanged<S, C>, U> unchanged) {
            return changed.apply(this);
        }

        @Override
        public boolean isChanged() {
            return true;
        }

        @Override
        @Nullable
        public Reply<S, C> getReply() {
            return reply;
        }
    }
}
