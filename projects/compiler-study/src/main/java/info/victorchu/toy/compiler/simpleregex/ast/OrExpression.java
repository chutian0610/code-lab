package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * or expression.
 *
 * @author victorchutian
 */
public class OrExpression
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

    public OrExpression()
    {
        super(NodeType.REGEX_OR);
    }

    @Override
    public <T, C> T accept(RegexExpressionVisitor<T, C> visitor, C context)
    {
        return visitor.visitOrNode(this, context);
    }

    /**
     * builder for  RegexOrNode
     */
    public static final class RegexOrNodeBuilder
    {
        private RegexExpression left;
        private RegexExpression right;

        private RegexOrNodeBuilder()
        {
        }

        public static RegexOrNodeBuilder aRegexOrNode()
        {
            return new RegexOrNodeBuilder();
        }

        public RegexOrNodeBuilder withLeft(RegexExpression left)
        {
            this.left = left;
            return this;
        }

        public RegexOrNodeBuilder withRight(RegexExpression right)
        {
            this.right = right;
            return this;
        }

        public OrExpression build()
        {
            OrExpression regexOrNode = new OrExpression();
            regexOrNode.setLeft(left);
            regexOrNode.setRight(right);
            return regexOrNode;
        }
    }
}