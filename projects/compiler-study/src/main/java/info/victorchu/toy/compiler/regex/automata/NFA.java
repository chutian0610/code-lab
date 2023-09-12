package info.victorchu.toy.compiler.regex.automata;

import info.victorchu.toy.compiler.regex.ast.ConcatExpression;
import info.victorchu.toy.compiler.regex.ast.OrExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionVisitor;
import info.victorchu.toy.compiler.regex.ast.RepeatExpression;
import info.victorchu.toy.compiler.regex.ast.CharExpression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * NFA Graph.
 *
 * @author victorchu
 */
public class NFA
{
    private static final NFABuilder BUILDER = new NFABuilder();

    public NFA(NFAState start, NFAState end)
    {
        this.start = start;
        this.end = end;
    }


    public NFAState start;
    public NFAState end;

    /**
     * 打印NFA状态图(mermaid.js 流程图语法).
     *
     * @return
     * @see <a href="https://mermaid.live/">https://mermaid.live</a>
     * @see <a href="https://mermaid.js.org/intro/n00b-gettingStarted.html">https://mermaid.js.org/intro/n00b-gettingStarted.html</a>
     */
    public String print()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("flowchart LR\n");
        Set<Integer> markSet = new HashSet<>();
        printState(start, sb, markSet);
        return sb.toString();
    }

    public static NFA buildNFA(RegexExpression regexExpression)
    {
        SubNFA subNFA = BUILDER.build(regexExpression, new Context());
        subNFA.end.setAccept(true);
        return new NFA(subNFA.start, subNFA.end);
    }

    private void printState(NFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            markSet.add(cursor.getId());
            List<Transition> transitions = cursor.getAllTransitionWithSort();
            for (Transition transition : transitions) {
                List<NFAState> stateSet = cursor.getTargetsOfTransitionSort(transition);
                for (NFAState state : stateSet) {
                    sb.append(cursor).append("-->|").append(transition).append("|").append(state).append("\n");
                    printState(state, sb, markSet);
                }
            }
        }
    }

    /**
     * NFA Sub Graph
     */
    private static class SubNFA
    {
        public SubNFA(NFAState start, NFAState end, Transition inTransition)
        {
            this.start = start;
            this.end = end;
            this.inTransition = inTransition;
        }
        public NFAState start;
        public Transition inTransition;
        public NFAState end;
    }

    /**
     * NFA Builder.
     * build NFA From Regex Expression.
     */
    private static class NFABuilder
            implements RegexExpressionVisitor<SubNFA, Context>
    {

        @Override
        public SubNFA visitChar(CharExpression node, Context context)
        {
            NFAState start = createNFAState(context);
            NFAState charState = createNFAState(context);
            start.addTransition(Transition.character(node.getCharacter()), charState);
            return new SubNFA(start, charState, Transition.epsilon());
        }

        @Override
        public SubNFA visitConcat(ConcatExpression node, Context context)
        {
            SubNFA subNFALeft = process(node.getLeft(), context);
            SubNFA subNFARight = process(node.getRight(), context);
            subNFALeft.end.addTransition(subNFARight.inTransition, subNFARight.start);
            return new SubNFA(subNFALeft.start, subNFARight.end, Transition.epsilon());
        }

        @Override
        public SubNFA visitOr(OrExpression node, Context context)
        {
            NFAState begin = createNFAState(context);
            SubNFA subNFALeft = process(node.getLeft(), context);
            begin.addTransition(subNFALeft.inTransition, subNFALeft.start);
            SubNFA subNFARight = process(node.getRight(), context);
            begin.addTransition(subNFARight.inTransition, subNFARight.start);
            NFAState end = createNFAState(context);
            subNFALeft.end.addTransition(Transition.epsilon(), end);
            subNFARight.end.addTransition(Transition.epsilon(), end);
            return new SubNFA(begin, end, Transition.epsilon());
        }

        @Override
        public SubNFA visitRepeat(RepeatExpression node, Context context)
        {
            NFAState begin = createNFAState(context);

            SubNFA subNFA = process(node.getInner(), context);
            begin.addTransition(subNFA.inTransition, subNFA.start);
            NFAState end = createNFAState(context);
            begin.addTransition(Transition.epsilon(), end);
            subNFA.end.addTransition(Transition.epsilon(), end);

            return new SubNFA(begin, end, Transition.epsilon());
        }

        public NFAState createNFAState(Context context)
        {
            return new NFAState(false, context::getNextID);
        }

        public SubNFA build(RegexExpression node, Context context)
        {
            return process(node, context);
        }
    }
}
