package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * concat expression.
 *
 * @author victorchutian
 */
public class ConcatExpression
        extends RegexExpression
{

    private RegexExpression left;
    private RegexExpression right;

    public RegexExpression getLeft()
    {
        return left;
    }

    public void setLeft(RegexExpression left)
    {
        this.left = left;
    }

    public RegexExpression getRight()
    {
        return right;
    }

    public void setRight(RegexExpression right)
    {
        this.right = right;
    }

    public ConcatExpression()
    {
        super(NodeType.REGEX_CONCAT);
    }

    @Override
    public <T, C> T accept(RegexExpressionVisitor<T, C> visitor, C context)
    {
        return visitor.visitConcatNode(this, context);
    }

    /**
     * builder for  RegexConcatNode
     */
    public static final class RegexConcatNodeBuilder
    {
        private RegexExpression left;
        private RegexExpression right;

        private RegexConcatNodeBuilder()
        {
        }

        public static RegexConcatNodeBuilder aRegexConcatNode()
        {
            return new RegexConcatNodeBuilder();
        }

        public RegexConcatNodeBuilder withLeft(RegexExpression left)
        {
            this.left = left;
            return this;
        }

        public RegexConcatNodeBuilder withRight(RegexExpression right)
        {
            this.right = right;
            return this;
        }

        public ConcatExpression build()
        {
            ConcatExpression regexConcatNode = new ConcatExpression();
            regexConcatNode.setLeft(left);
            regexConcatNode.setRight(right);
            return regexConcatNode;
        }
    }
}

