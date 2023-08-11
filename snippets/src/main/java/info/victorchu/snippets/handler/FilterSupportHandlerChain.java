package info.victorchu.snippets.handler;

import java.util.List;

/**
 * @author victorchu
 * @date 2022/9/14 15:04
 */
public class FilterSupportHandlerChain implements HandlerChain {
    private int index;
    private final List<AbstractFilterSupportHandler.Filter> filters;
    private final AbstractFilterSupportHandler.Handler handler;


    public FilterSupportHandlerChain(List<AbstractFilterSupportHandler.Filter> filters, AbstractFilterSupportHandler.Handler handler) {
        if (filters == null || handler == null){
            throw new IllegalArgumentException();
        }
        index = 0;
        this.filters = filters;
        this.handler = handler;
    }

    @Override
    public void process(HandlerContext context) {
        while (this.index < filters.size()) {
            AbstractFilterSupportHandler.Filter filter = filters.get(this.index);
            this.index++;
            filter.preProcess(context);
        }
        handler.process(context);
        while (this.index>0){
            this.index--;
            AbstractFilterSupportHandler.Filter filter = filters.get(this.index);
            filter.postProcess(context);
        }

    }
}
