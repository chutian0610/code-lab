package info.victorchu.toy.compiler.regex.automata;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author victorchu
 * @date 2023/9/12 20:45
 */
public class Context
{
    /**
     * state id generator
     */
    private AtomicInteger NEXT_ID = new AtomicInteger(0);

    public Context()
    {

    }

    public synchronized void reset()
    {
        NEXT_ID = new AtomicInteger();
    }

    public Integer getNextID()
    {
        return NEXT_ID.getAndIncrement();
    }
}
