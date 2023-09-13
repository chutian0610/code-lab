package info.victorchu.toy.compiler.regex.automata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    private AtomicInteger NEXT_NFA_ID;
    private AtomicInteger NEXT_DFA_ID;

    private Map<Integer, NFAState> nfaStateMap = new HashMap<>();
    private Map<Integer, DFAState> dfaStateMap = new HashMap<>();
    private Map<Set<NFAState>, DFAState> nfa2dfaStateMap = new HashMap<>();

    public Context()
    {
        reset();
    }

    public synchronized void reset()
    {
        NEXT_NFA_ID = new AtomicInteger(0);
        NEXT_DFA_ID = new AtomicInteger(0);
        nfaStateMap.clear();
        dfaStateMap.clear();
    }

    public Integer getNextNFAID()
    {
        return NEXT_NFA_ID.getAndIncrement();
    }

    public Integer getNextDFAID()
    {
        return NEXT_DFA_ID.getAndIncrement();
    }

    public void registerNFAState(NFAState nfaState)
    {
        nfaStateMap.put(nfaState.getId(), nfaState);
    }

    public NFAState getNFAState(Integer nfaStateId)
    {
        return nfaStateMap.get(nfaStateId);
    }

    public void registerDFAState(DFAState dfaState)
    {
        dfaStateMap.put(dfaState.getId(), dfaState);
    }

    public DFAState getDFAState(Integer dfaStateId)
    {
        return dfaStateMap.get(dfaStateId);
    }

    public DFAState getDFAState(Set<NFAState> nfaStateSet)
    {
        return nfa2dfaStateMap.get(nfaStateSet);
    }

    public DFAState createDFAState(Set<NFAState> nfaStateSet)
    {
        if (nfa2dfaStateMap.containsKey(nfaStateSet)) {
            return nfa2dfaStateMap.get(nfaStateSet);
        }
        else {
            DFAState dfaState = new DFAState(this, nfaStateSet, isAccept(nfaStateSet));
            nfa2dfaStateMap.put(nfaStateSet, dfaState);
            return dfaState;
        }
    }

    public NFAState createNFAState()
    {
        return new NFAState(this);
    }

    /**
     * 判断 NFA 状态集合 S 是否为 DFA 的接受状态。
     */
    private static boolean isAccept(Set<NFAState> states)
    {
        return states.stream().anyMatch(NFAState::isAccept);
    }
}
