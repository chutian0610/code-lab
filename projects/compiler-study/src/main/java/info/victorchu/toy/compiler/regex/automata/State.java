package info.victorchu.toy.compiler.regex.automata;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 代表 自动机的状态.
 *
 * @author victorchu
 */
public class State
{
    /**
     * 是否是可接受状态
     */
    private boolean accept;
    /**
     * state id ( must unique )
     */
    private final int id;

    public State(boolean accept, Supplier<Integer> stateIdSupplier)
    {
        this.id = stateIdSupplier.get();
        this.accept = accept;
    }

    public int getId()
    {
        return id;
    }

    public boolean isAccept()
    {
        return accept;
    }

    public void setAccept(boolean accept)
    {
        this.accept = accept;
    }
}
