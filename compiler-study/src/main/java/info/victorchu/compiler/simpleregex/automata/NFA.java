package info.victorchu.compiler.simpleregex.automata;

import java.util.ArrayList;
import java.util.List;

/**
 * NFA Graph.
 * @author victorchu
 * @date 2022/6/28 5:26 下午
 */
public class NFA {
    private final List<NFAState> stateList = new ArrayList<>();

    public NFA(){
        stateList.add(new NFABeginState());
    }

    public List<NFAState> getStateList() {
        return stateList;
    }

    public void addState(NFAState nfaState){
        stateList.add(nfaState);
    }

    public NFAState getLastNFAState(){
        return stateList.get(stateList.size()-1);
    }
}
