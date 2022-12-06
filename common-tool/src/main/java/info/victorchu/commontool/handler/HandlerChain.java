package info.victorchu.commontool.handler;

/**
 * @author victorchu
 * @date 2022/9/14 14:37
 */
public interface HandlerChain<Context extends HandlerContext> {
    void process(Context context);
}