package info.victorchu.toy.compiler.regex.automata;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 代表 自动机的状态.
 *
 * @author victorchu
 */
public class State
{
    /**
     * state id generator
     */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(0);
    /**
     * 是否是可接受状态
     */
    private boolean accept;
    /**
     * state id ( must unique )
     */
    private final int id;

    public State(boolean accept)
    {
        this.id = NEXT_ID.getAndIncrement();
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
