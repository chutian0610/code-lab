package info.victorchu.compiler.simpleregex;

/**
 * 可重复节点.
 * @date 2022/2/11 5:19 下午
 * @author victorchutian
 */
public class RegexRepeatNode extends RegexNode{
    public RegexRepeatNode() {
        super(NodeType.REGEXP_REPEAT);
    }
    private RegexNode innerNode;

    public RegexNode getInnerNode() {
        return innerNode;
    }

    public void setInnerNode(RegexNode innerNode) {
        this.innerNode = innerNode;
    }

    @Override
    public <T> T accept(RegexNodeVisitor<T> visitor){
        return visitor.visit(this);
    }

    /**
     * builder for  RegexRepeatNode
     */
    public static final class RegexRepeatNodeBuilder {
        private RegexNode innerNode;

        private RegexRepeatNodeBuilder() {
        }

        public static RegexRepeatNodeBuilder aRegexRepeatNode() {
            return new RegexRepeatNodeBuilder();
        }


        public RegexRepeatNodeBuilder withInnerNode(RegexNode innerNode) {
            this.innerNode = innerNode;
            return this;
        }

        public RegexRepeatNode build() {
            RegexRepeatNode regexRepeatNode = new RegexRepeatNode();
            regexRepeatNode.setInnerNode(innerNode);
            return regexRepeatNode;
        }
    }
}
