package info.victorchu.toy.compiler.simpleregex.ast;

import info.victorchu.toy.compiler.simpleregex.util.Pair;

import java.util.LinkedList;
import java.util.Optional;

/**
 * print regex node tree in terminal
 *
 * @author victorchu
 */
public class RegexNodePrintVisitor
        implements RegexNodeVisitor<Object, Pair<RegexNodePrintVisitor.PrintStackContext, Boolean>>
{
    public String build(RegexNode node)
    {
        PrintStackContext context = new PrintStackContext();
        this.process(node, Pair.of(context, false));
        return context.sb.toString();
    }

    @Override
    public Object visitCharNode(RegexCharNode node, Pair<RegexNodePrintVisitor.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        context.getLeft().pop();

        return null;
    }

    @Override
    public Object visitConcatNode(RegexConcatNode node, Pair<RegexNodePrintVisitor.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitOrNode(RegexOrNode node, Pair<RegexNodePrintVisitor.PrintStackContext, Boolean> context)
    {
        context.getLeft().push(node, context.getRight());
        context.getLeft().sb.append(context.getLeft().peek().get().toString());
        process(node.getLeft(), Pair.of(context.getLeft(), false));
        process(node.getRight(), Pair.of(context.getLeft(), true));
        context.getLeft().pop();
        return null;
    }

    @Override
    public Object visitRepeatNode(RegexRepeatNode node, Pair<RegexNodePrintVisitor.PrintStackContext, Boolean> context)
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

        public void push(RegexNode node, boolean last)
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
        public static StackItem of(RegexNode node, boolean last, StackItem parent)
        {
            return new StackItem(node, last, parent);
        }

        public static StackItem root(RegexNode node)
        {
            return new StackItem(node, false, null);
        }

        private final RegexNode node;
        private final boolean last;

        private final StackItem parent;

        private StackItem(RegexNode node, boolean last, StackItem parent)
        {
            this.node = node;
            this.parent = parent;
            this.last = last;
        }

        public RegexNode getNode()
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
                return RegexNodeFormatter.formatter.process(node, null) + "\n";
            }
            String nodeStr = RegexNodeFormatter.formatter.process(node, null) + "\n";
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

    public static class RegexNodeFormatter
            implements RegexNodeVisitor<String, Void>
    {
        public static RegexNodeFormatter formatter = new RegexNodeFormatter();

        @Override
        public String visitCharNode(RegexCharNode node, Void context)
        {
            return "Node[Char](" + node.getCharacter() + ")";
        }

        @Override
        public String visitConcatNode(RegexConcatNode node, Void context)
        {
            return "Node[Concat]";
        }

        @Override
        public String visitOrNode(RegexOrNode node, Void context)
        {
            return "Node[Or]";
        }

        @Override
        public String visitRepeatNode(RegexRepeatNode node, Void context)
        {
            return "Node[Repeat]";
        }
    }
}
