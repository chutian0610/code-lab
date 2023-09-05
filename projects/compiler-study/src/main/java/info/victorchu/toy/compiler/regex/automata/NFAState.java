package info.victorchu.toy.compiler.regex.automata;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * NFA 状态，在State上封装了 Transition 映射.
 *
 * @author victorchu
 * @date 2023/9/5 15:34
 */
public class NFAState
{
    private final State state;
    private final Map<Transition, Set<NFAState>> transitions;

    public void addTransition(Transition transition, NFAState target)
    {
        Set<NFAState> stateSet = transitions.computeIfAbsent(transition, k -> new HashSet<>());
        stateSet.add(target);
    }

    @Nonnull
    public Set<NFAState> getTargetsOfTransition(Transition transition)
    {
        return transitions.getOrDefault(transition, new HashSet<>(0));
    }

    @Nonnull
    public Set<Transition> getAllTransition()
    {
        return transitions.keySet();
    }

    public NFAState(boolean accept)
    {
        this.state = new State(accept);
        transitions = new HashMap<>();
    }

    public NFAState()
    {
        this.state = new State(false);
        transitions = new HashMap<>();
    }

    public int getId()
    {
        return state.getId();
    }

    public boolean isAccept()
    {
        return state.isAccept();
    }

    public void setAccept(boolean accept)
    {
        state.setAccept(accept);
    }

    @Override
    public String toString()
    {

        if (state.isAccept()) {
            return String.format("s_%d((%d))", state.getId(), state.getId());
        }
        else {
            return String.format("s_%d(%d)", state.getId(), state.getId());
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NFAState)) {
            return false;
        }
        NFAState nfaState = (NFAState) o;
        return Objects.equals(state, nfaState.state);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(state);
    }
}
