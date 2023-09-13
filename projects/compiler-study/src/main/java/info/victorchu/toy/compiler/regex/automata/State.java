package info.victorchu.toy.compiler.regex.automata;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


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

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        State state = (State) o;
        return Objects.equals(state.id, this.id);
    }
}
