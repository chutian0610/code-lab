package info.victorchu.toy.compiler.regex.automata;

import info.victorchu.toy.compiler.regex.ast.ConcatExpression;
import info.victorchu.toy.compiler.regex.ast.OrExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionVisitor;
import info.victorchu.toy.compiler.regex.ast.RepeatExpression;
import info.victorchu.toy.compiler.regex.ast.CharExpression;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NFA Graph.
 *
 * @author victorchu
 */
public class NFA
{
    private static final NFABuilder BUILDER = new NFABuilder();

    public NFA(NFAState start, Context context)
    {
        this.start = start;
        this.context = context;
    }

    private final Context context;
    private final NFAState start;

    // ================================ NFA to DFA =============================================
    private Set<NFAState> findEpsilonClosure(NFAState start)
    {
        return findEpsilonClosure(start, new HashSet<>());
    }

    /**
     * 找到 start 状态的未被标记的 ϵ 闭包
     *
     * @param start
     * @param marked
     * @return
     */
    private static Set<NFAState> findEpsilonClosure(NFAState start, Set<NFAState> marked)
    {
        Set<NFAState> result = new HashSet<>();
        if (!marked.contains(start)) {
            // 记录当前节点
            result.add(start);
        }
        start.getTargetsOfTransitionSort(EpsilonTransition.INSTANCE)
                .stream()
                // 记录未标记节点
                .filter(s -> !marked.contains(s))
                .map(s -> {
                    result.add(s);
                    marked.add(s);
                    // 记录下一级节点的 ϵ 闭包
                    return findEpsilonClosure(s, marked);
                }).forEach(result::addAll);
        return result;
    }

    /**
     * NFA 转DFA
     *
     * @return
     */
    public DFA toDFA()
    {
        Set<NFAState> startSet = findEpsilonClosure(this.start);
        DFAState start = createDFAState(startSet);
        return new DFA(start, context);
    }

    private DFAState createDFAState(Set<NFAState> nfaSet)
    {
        // 防止循环递归
        if (context.getDFAState(nfaSet) != null) {
            return context.getDFAState(nfaSet);
        }
        // 构建对应的DFA节点
        DFAState start = context.createDFAState(nfaSet);
        Map<Transition, Set<NFAState>> map = findDFAMoveTable(nfaSet);
        if (!map.isEmpty()) {
            // 设置DFA跳转状态
            map.entrySet()
                    .stream()
                    .filter((x) -> !start.containTransition(x.getKey()))
                    .forEach(x -> {
                        if (x.getValue().equals(nfaSet)) {
                            start.addTransition(x.getKey(), start);
                        }
                        else {
                            DFAState state = createDFAState(x.getValue());
                            start.addTransition(x.getKey(), state);
                        }
                    });
        }
        return start;
    }

    private Map<Transition, Set<NFAState>> findDFAMoveTable(Set<NFAState> nfaSet)
    {
        // 获取对当前状态集有效的字符集
        Set<Transition> transitionSet = nfaSet.stream()
                .map(NFAState::getAllTransitionExceptEpsilon)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Transition, Set<NFAState>> res = new HashMap<>();
        for (Transition transition : transitionSet) {
            res.put(transition, findDFAMoveSet(nfaSet, transition));
        }
        return res;
    }

    private Set<NFAState> findDFAMoveSet(Set<NFAState> nfaSet, Transition transition)
    {
        Set<NFAState> res = new HashSet<>();
        for (NFAState s : nfaSet) {
            List<NFAState> next = s.getTargetsOfTransitionSort(transition);
            if (!next.isEmpty()) {
                next.forEach(x -> {
                    res.addAll(findEpsilonClosure(x));
                });
            }
        }
        return res;
    }

    // ================================print NFA =============================================

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

    private static void printState(NFAState cursor, StringBuilder sb, Set<Integer> markSet)
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

    // ================================build NFA =============================================

    public static NFA buildNFA(RegexExpression regexExpression)
    {
        Context context = new Context();
        SubNFA subNFA = BUILDER.build(regexExpression, context);
        subNFA.end.setAccept(true);
        return new NFA(subNFA.start, context);
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
            NFAState start = context.createNFAState();
            NFAState charState = context.createNFAState();
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
            NFAState begin = context.createNFAState();
            SubNFA subNFALeft = process(node.getLeft(), context);
            begin.addTransition(subNFALeft.inTransition, subNFALeft.start);
            SubNFA subNFARight = process(node.getRight(), context);
            begin.addTransition(subNFARight.inTransition, subNFARight.start);
            NFAState end = context.createNFAState();
            subNFALeft.end.addTransition(Transition.epsilon(), end);
            subNFARight.end.addTransition(Transition.epsilon(), end);
            return new SubNFA(begin, end, Transition.epsilon());
        }

        @Override
        public SubNFA visitRepeat(RepeatExpression node, Context context)
        {
            NFAState begin = context.createNFAState();
            SubNFA subNFA = process(node.getInner(), context);
            NFAState end = context.createNFAState();

            // 一次
            begin.addTransition(subNFA.inTransition, subNFA.start);
            subNFA.end.addTransition(Transition.epsilon(), end);
            // 多次
            subNFA.end.addTransition(Transition.epsilon(), subNFA.start);

            // 0次
            begin.addTransition(Transition.epsilon(),end);
            return new SubNFA(begin, end, Transition.epsilon());
        }

        public SubNFA build(RegexExpression node, Context context)
        {
            return process(node, context);
        }
    }
}
