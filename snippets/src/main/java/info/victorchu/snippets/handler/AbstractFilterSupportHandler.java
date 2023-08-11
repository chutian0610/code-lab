package info.victorchu.snippets.handler;

/**
 * @author victorchu
 * @date 2022/9/14 15:00
 */
public abstract class AbstractFilterSupportHandler implements Handler<HandlerContext, FilterSupportHandlerChain>{

    protected void preProcess(HandlerContext context){

    }

    protected void postProcess(HandlerContext context){

    }

    protected void process(HandlerContext context){

    }

    static abstract class Filter extends AbstractFilterSupportHandler{
        @Override
        protected abstract void postProcess(HandlerContext context);
        @Override
        protected abstract void preProcess(HandlerContext context);
    }
    static abstract class Handler extends AbstractFilterSupportHandler{
        @Override
        protected abstract void process(HandlerContext context);
    }
}
