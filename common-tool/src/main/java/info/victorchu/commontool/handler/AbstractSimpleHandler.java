package info.victorchu.commontool.handler;

/**
 * 简单的handler.
 * 实现类只需要实现innerProcess方法。
 * @author victorchu
 * @date 2022/9/14 14:39
 */
public abstract class AbstractSimpleHandler implements Handler<HandlerContext,SimpleHandlerChain>{

    public void process(HandlerContext context, SimpleHandlerChain chain) {
        if(match(context)){
            innerProcess(context, chain);
        } else {
            chain.process(context);
        }
    }
    protected abstract void innerProcess(HandlerContext context, HandlerChain chain);

    protected abstract boolean match(HandlerContext queryContext);
}
