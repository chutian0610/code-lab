package info.victorchu.toy.compiler.regex.automata;

import info.victorchu.toy.compiler.regex.util.Pair;

import javax.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author victorchu
 * @date 2023/9/13 16:13
 */
public class DFAState
{
    private final State state;

    /**
     * DFA的状态变更是确定的
     */
    private final Map<Transition, DFAState> transitions;

    /**
     * DFA 对应 NFA 状态集合
     */
    private final Set<NFAState> nfaSets;

    public DFAState(Context context, Set<NFAState> nfaSets, boolean isAccept)
    {
        this.state = new State(isAccept, context::getNextDFAID);
        this.nfaSets = nfaSets;
        this.transitions = new HashMap<>();
        context.registerDFAState(this);
    }

    public void addTransition(Transition transition, DFAState target)
    {
        transitions.put(transition, target);
    }

    public boolean containTransition(Transition transition)
    {
        return transitions.containsKey(transition);
    }

    @Nonnull
    public Optional<DFAState> getTargetOfTransition(Transition transition)
    {
        return Optional.ofNullable(transitions.get(transition));
    }

    @Nonnull
    public List<Transition> getAllTransitionWithSort()
    {
        return transitions.keySet().stream().map(x -> {
            Integer weight = transitions.get(x).getId();
            return Pair.of(x, weight);
        }).sorted((o1, o2) -> o2.getRight().compareTo(o1.getRight())).map(Pair::getLeft).collect(Collectors.toList());
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
            return String.format("ds_%d((%d))", state.getId(), state.getId());
        }
        else {
            return String.format("ds_%d(%d)", state.getId(), state.getId());
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DFAState)) {
            return false;
        }
        DFAState dfaState = (DFAState) o;
        return Objects.equals(state, dfaState.state);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(state);
    }

    public int getId()
    {
        return state.getId();
    }

    public Set<NFAState> getNfaSets()
    {
        return nfaSets;
    }
}
