package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * 重复 expression.
 *
 * @author victorchutian
 */
public class RepeatExpression
        extends RegexExpression
{
    public RepeatExpression()
    {
        super(NodeType.REGEXP_REPEAT);
    }

    private RegexExpression inner;

    public RegexExpression getInner()
    {
        return inner;
    }

    public void setInner(RegexExpression inner)
    {
        this.inner = inner;
    }

    @Override
    public <T, C> T accept(RegexExpressionVisitor<T, C> visitor, C context)
    {
        return visitor.visitRepeat(this, context);
    }

    /**
     * builder for  RegexRepeatNode
     */
    public static final class RegexRepeatNodeBuilder
    {
        private RegexExpression innerNode;

        private RegexRepeatNodeBuilder()
        {
        }

        public static RegexRepeatNodeBuilder aRegexRepeatNode()
        {
            return new RegexRepeatNodeBuilder();
        }

        public RegexRepeatNodeBuilder withInnerNode(RegexExpression innerNode)
        {
            this.innerNode = innerNode;
            return this;
        }

        public RepeatExpression build()
        {
            RepeatExpression regexRepeatNode = new RepeatExpression();
            regexRepeatNode.setInner(innerNode);
            return regexRepeatNode;
        }
    }
}
