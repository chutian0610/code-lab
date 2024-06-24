package info.victorchu.snippets.handler;

import java.util.List;

/**
 * @author victorchu

 */
public class SimpleHandlerChain implements HandlerChain{
    private int index;
    private final List<AbstractSimpleHandler> simpleHandlers;
    public SimpleHandlerChain(List<AbstractSimpleHandler> simpleHandlers) {
        if (simpleHandlers == null){
            throw new IllegalArgumentException();
        }
        this.index = 0;
        this.simpleHandlers = simpleHandlers;
    }
    @Override
    public void process(HandlerContext context) {
        if (this.index < simpleHandlers.size()) {
            AbstractSimpleHandler handler = simpleHandlers.get(this.index);
            this.index++;
            handler.process(context, this);
        }
    }
}
