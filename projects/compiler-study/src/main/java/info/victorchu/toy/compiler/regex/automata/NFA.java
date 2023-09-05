package info.victorchu.toy.compiler.regex.automata;

import info.victorchu.toy.compiler.regex.ast.ConcatExpression;
import info.victorchu.toy.compiler.regex.ast.OrExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionVisitor;
import info.victorchu.toy.compiler.regex.ast.RepeatExpression;
import info.victorchu.toy.compiler.regex.ast.CharExpression;

import java.util.HashSet;
import java.util.Set;

/**
 * NFA Graph.
 *
 * @author victorchu
 */
public class NFA
{
    public static final NFABuilder BUILDER = new NFABuilder();

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
        return BUILDER.buildNFA(regexExpression);
    }

    public void printState(NFAState cursor, StringBuilder sb, Set<Integer> markSet)
    {
        if (cursor != null && !markSet.contains(cursor.getId())) {
            markSet.add(cursor.getId());
            Set<Transition> transitionSet = cursor.getAllTransition();
            for (Transition transition : transitionSet) {
                Set<NFAState> stateSet = cursor.getTargetsOfTransition(transition);
                for (NFAState state : stateSet) {
                    sb.append(cursor).append("-->|").append(transition).append("|").append(state).append("\n");
                    printState(state, sb, markSet);
                }
            }
        }
    }

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

    private static class NFABuilder
            implements RegexExpressionVisitor<SubNFA, Void>
    {

        @Override
        public SubNFA visitChar(CharExpression node, Void context)
        {
            NFAState start = new NFAState();
            NFAState charState = new NFAState();
            start.addTransition(Transition.character(node.getCharacter()), charState);
            return new SubNFA(start, charState, Transition.epsilon());
        }

        @Override
        public SubNFA visitConcat(ConcatExpression node, Void context)
        {
            SubNFA subNFALeft = process(node.getLeft(), context);
            SubNFA subNFARight = process(node.getRight(), context);
            subNFALeft.end.addTransition(subNFARight.inTransition, subNFARight.start);
            return new SubNFA(subNFALeft.start, subNFARight.end, Transition.epsilon());
        }

        @Override
        public SubNFA visitOr(OrExpression node, Void context)
        {
            NFAState begin = new NFAState();
            SubNFA subNFALeft = process(node.getLeft(), context);
            begin.addTransition(subNFALeft.inTransition, subNFALeft.start);
            SubNFA subNFARight = process(node.getRight(), context);
            begin.addTransition(subNFARight.inTransition, subNFARight.start);
            NFAState end = new NFAState();
            subNFALeft.end.addTransition(Transition.epsilon(), end);
            subNFARight.end.addTransition(Transition.epsilon(), end);
            return new SubNFA(begin, end, Transition.epsilon());
        }

        @Override
        public SubNFA visitRepeat(RepeatExpression node, Void context)
        {
            NFAState begin = new NFAState();

            SubNFA subNFA = process(node.getInner(), context);
            begin.addTransition(subNFA.inTransition, subNFA.start);
            NFAState end = new NFAState();
            begin.addTransition(Transition.epsilon(), end);
            subNFA.end.addTransition(Transition.epsilon(), end);

            return new SubNFA(begin, end, Transition.epsilon());
        }

        public NFA buildNFA(RegexExpression node)
        {
            SubNFA subNFA = process(node, null);
            subNFA.end.setAccept(true);
            return new NFA(subNFA.start, subNFA.end);
        }
    }
}
