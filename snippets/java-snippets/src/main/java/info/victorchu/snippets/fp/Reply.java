package info.victorchu.snippets.fp;

import lombok.SneakyThrows;

import java.util.function.Function;

public abstract class Reply<S, C> {

    public static <S, C> Ok<S, C> ok(S result, C context, Message msg) {
        return new Ok<S, C>(result, context, msg);
    }

    public static <S, C> Ok<S, C> ok(S result, C context) {
        return new Ok<S, C>(result, context, Message.of());
    }


    public static <S, C> Error<S, C> error(C context, Message msg,Throwable t) {
        return new Error<S, C>(context, msg,t);
    }
    public static <S, C> Error<S, C> error(C context, Message msg) {
        return new Error<S, C>(context, msg);
    }

    public final Message msg;

    Reply(Message msg) {
        this.msg = msg;
    }

    public abstract S getResult();

    public abstract C getContext();

    public abstract <U> U match(Function<Ok<S, C>, U> ok, Function<Error<S, C>, U> error);

    public static final class Ok<S, C> extends Reply<S, C> {
        public final S result;
        public final C context;

        Ok(S result, C context, Message msg) {
            super(msg);
            this.result = result;
            this.context = context;
        }

        @Override
        public <U> U match(Function<Ok<S, C>, U> ok, Function<Error<S, C>, U> error) {
            return ok.apply(this);
        }

        @Override
        public S getResult() {
            return result;
        }


        @Override
        public C getContext() {
            return context;
        }
    }

    public static final class Error<S, C> extends Reply<S, C> {
        public final C context;

        public Throwable getCause() {
            return cause;
        }

        public final Throwable cause;

        Error(C context, Message msg,Throwable cause) {
            super(msg);
            this.context = context;
            this.cause = cause;
        }

        Error(C context, Message msg) {
            super(msg);
            this.context = context;
            this.cause = null;
        }
        public <T> Reply<T, C> cast() {
            return (Error<T, C>)this;
        }
        @Override
        public <B> B match(Function<Ok<S, C>, B> ok, Function<Error<S, C>, B> error) {
            return error.apply(this);
        }

        @Override
        @SneakyThrows(TransformException.class)
        public S getResult() {
            if(cause == null) {
                throw new TransformException(msg.toString());
            }
            throw new TransformException(msg.toString(),cause);
        }

        @Override
        public C getContext() {
            return context;
        }
    }
}
