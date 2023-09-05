package info.victorchu.toy.compiler.simpleregex.ast;

import info.victorchu.toy.compiler.simpleregex.util.Pair;

import java.util.LinkedList;
import java.util.Optional;

/**
 * print regex node tree in terminal
 *
 * @author victorchu
 */
public class RegexExpressionTreePrinter
        implements RegexExpressionVisitor<Object, Pair<RegexExpressionTreePrinter.PrintStackContext, Boolean>>
{
    public String build(RegexExpression node)
    {
        PrintStackContext context = new PrintStackContext();
        this.process(node, Pair.of(context, false));
        return context.sb.toString();
    }

    @Override
    public Object visitCharNode(CharExpression node, Pair<RegexExpressionTreePrinter.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        context.getLeft().pop();

        return null;
    }

    @Override
    public Object visitConcatNode(ConcatExpression node, Pair<RegexExpressionTreePrinter.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitOrNode(OrExpression node, Pair<RegexExpressionTreePrinter.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitRepeatNode(RepeatExpression node, Pair<RegexExpressionTreePrinter.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        process(node.getInnerNode(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    public static class PrintStackContext
    {

        private final LinkedList<StackItem> stack;
        private final StringBuilder sb;

        public PrintStackContext()
        {
            stack = new LinkedList<>();
            sb = new StringBuilder();
        }

        public void push(RegexExpression node, boolean last)
        {
            StackItem wrapper;
            if (stack.isEmpty()) {
                wrapper = StackItem.root(node);
            }
            else {
                wrapper = StackItem.of(node, last, stack.peek());
            }
            stack.push(wrapper);
        }

        public Optional<StackItem> pop()
        {
            if (stack.isEmpty()) {
                return Optional.empty();
            }
            return Optional.ofNullable(stack.pop());
        }

        public Optional<StackItem> peek()
        {
            return Optional.ofNullable(stack.peek());
        }
    }

    public static class StackItem
    {
        public static StackItem of(RegexExpression node, boolean last, StackItem parent)
        {
            return new StackItem(node, last, parent);
        }

        public static StackItem root(RegexExpression node)
        {
            return new StackItem(node, false, null);
        }

        private final RegexExpression node;
        private final boolean last;

        private final StackItem parent;

        private StackItem(RegexExpression node, boolean last, StackItem parent)
        {
            this.node = node;
            this.parent = parent;
            this.last = last;
        }

        public RegexExpression getNode()
        {
            return node;
        }

        public StackItem getParent()
        {
            return parent;
        }

        public boolean isRoot()
        {
            return parent == null;
        }

        public boolean isLast()
        {
            return last;
        }

        @Override
        public String toString()
        {
            if (parent == null) {
                return RegexExpressionFormatter.formatter.process(node, null) + "\n";
            }
            String nodeStr = RegexExpressionFormatter.formatter.process(node, null) + "\n";
            StackItem cursor = this;
            if (cursor.last) {
                nodeStr = "└──" + nodeStr;
            }
            else {
                nodeStr = "├──" + nodeStr;
            }
            cursor = cursor.parent;
            while (cursor.parent != null) {
                if (cursor.last) {
                    nodeStr = "   " + nodeStr;
                }
                else {
                    nodeStr = "│  " + nodeStr;
                }
                cursor = cursor.parent;
            }
            return nodeStr;
        }
    }

    public static class RegexExpressionFormatter
            implements RegexExpressionVisitor<String, Void>
    {
        public static RegexExpressionFormatter formatter = new RegexExpressionFormatter();

        @Override
        public String visitCharNode(CharExpression node, Void context)
        {
            return "[Char] : " + node.getCharacter();
        }

        @Override
        public String visitConcatNode(ConcatExpression node, Void context)
        {
            return "[Concat]";
        }

        @Override
        public String visitOrNode(OrExpression node, Void context)
        {
            return "[Or]";
        }

        @Override
        public String visitRepeatNode(RepeatExpression node, Void context)
        {
            return "[Repeat]";
        }
    }
}
