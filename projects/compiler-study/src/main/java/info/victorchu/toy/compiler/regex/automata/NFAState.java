package info.victorchu.toy.compiler.regex.automata;

import info.victorchu.toy.compiler.regex.util.Pair;
import info.victorchu.toy.compiler.regex.util.Tuple3;

import javax.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public List<NFAState> getTargetsOfTransitionSort(Transition transition)
    {
        return transitions.getOrDefault(transition, new HashSet<>(0)).stream().sorted(Comparator.comparing(NFAState::getId)).collect(Collectors.toList());
    }

    @Nonnull
    public List<Transition> getAllTransitionWithSort()
    {
        return transitions.keySet().stream()
                .map(x -> {
                    Long weight = transitions.get(x).stream().reduce(0L, (y, z) -> y + z.getId(), Long::sum);
                    Long count = transitions.get(x).stream().reduce(0L, (y, z) -> y + 1, Long::sum);
                    return Tuple3.of(x, count, weight);
                })
                .sorted(((Comparator<Tuple3<Transition, Long, Long>>) (o1, o2) -> o2.getT2().compareTo(o1.getT2())).thenComparing((o1, o2) -> o2.getT3().compareTo(o1.getT3())))
                .map(Tuple3::getT1).collect(Collectors.toList());
    }

    public NFAState(boolean accept, Supplier<Integer> stateIdSupplier)
    {
        this.state = new State(accept, stateIdSupplier);
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
