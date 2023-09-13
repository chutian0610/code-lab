package info.victorchu.toy.compiler.regex.automata;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author victorchu
 * @date 2023/9/5 17:12
 */
public class DFA
{
    private final Context context;
    private final DFAState start;

    public DFA(DFAState start, Context context)
    {
        this.start = start;
        this.context = context;
    }

    // ================================ simplify DFA =========================================

    /**
     * DFA 化简
     */
    public DFA simplify()
    {
        
        return null;
    }

    // ================================ print DFA =============================================

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @return
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Set<Integer> markSet = new HashSet<>();
        printState(start, sb, markSet);
        return sb.toString();
    }

    private static void printState(DFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            markSet.add(cursor.getId());
            List<Transition> transitions = cursor.getAllTransitionWithSort();
            for (Transition transition : transitions) {
                Optional<DFAState> state = cursor.getTargetOfTransition(transition);
                if (state.isPresent()) {
                    sb.append(cursor).append("-->|").append(transition).append("|").append(state.get()).append("\n");
                    printState(state.get(), sb, markSet);
                }
            }
        }
    }

    public String printMapping()
    {
        StringBuilder sb = new StringBuilder();
        Set<Integer> markSet = new HashSet<>();
        printMapping(start, sb, markSet);
        return sb.toString();
    }

    private static void printMapping(DFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            String nfaStr = String.format("(%s)", cursor.getNfaSets().stream().map(x -> "ns_" + x.getId()).collect(Collectors.joining(",")));
            sb.append("ds_").append(cursor.getId()).append("<==>").append(nfaStr).append("\n");
            markSet.add(cursor.getId());
            List<Transition> transitions = cursor.getAllTransitionWithSort();
            for (Transition transition : transitions) {
                Optional<DFAState> state = cursor.getTargetOfTransition(transition);
                state.ifPresent(dfaState -> printMapping(dfaState, sb, markSet));
            }
        }
    }

}
