package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * 或节点.
 * @date 2022/2/11 5:18 下午
 * @author victorchutian
 */
public class RegexOrNode extends RegexNode{


    private RegexNode left;
    private RegexNode right;

    public RegexNode getLeft() {
        return left;
    }

    public void setLeft(RegexNode left) {
        this.left = left;
    }

    public RegexNode getRight() {
        return right;
    }

    public void setRight(RegexNode right) {
        this.right = right;
    }

    public RegexOrNode() {
        super(NodeType.REGEX_OR);
    }
    @Override
    public <T,C> T accept(RegexNodeVisitor<T,C> visitor,C context){
        return visitor.visitOrNode(this,context);
    }

    /**
     * builder for  RegexOrNode
     */
    public static final class RegexOrNodeBuilder {
        private RegexNode left;
        private RegexNode right;

        private RegexOrNodeBuilder() {
        }

        public static RegexOrNodeBuilder aRegexOrNode() {
            return new RegexOrNodeBuilder();
        }

        public RegexOrNodeBuilder withLeft(RegexNode left) {
            this.left = left;
            return this;
        }

        public RegexOrNodeBuilder withRight(RegexNode right) {
            this.right = right;
            return this;
        }

        public RegexOrNode build() {
            RegexOrNode regexOrNode = new RegexOrNode();
            regexOrNode.setLeft(left);
            regexOrNode.setRight(right);
            return regexOrNode;
        }
    }
}