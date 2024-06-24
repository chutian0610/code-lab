package info.victorchu.snippets.handler;

/**
 * @author victorchu

 */
public interface HandlerChain<Context extends HandlerContext> {
    void process(Context context);
}
