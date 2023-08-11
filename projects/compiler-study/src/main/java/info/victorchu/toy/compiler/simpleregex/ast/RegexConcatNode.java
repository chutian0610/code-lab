package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * 连接节点.
 * @date 2022/2/11 5:18 下午
 * @author victorchutian
 */
public class RegexConcatNode extends RegexNode {

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

    public RegexConcatNode() {
        super(NodeType.REGEX_CONCAT);
    }
    @Override
    public <T,C> T accept(RegexNodeVisitor<T,C> visitor,C context){
        return visitor.visitConcatNode(this,context);
    }


    /**
     * builder for  RegexConcatNode
     */
    public static final class RegexConcatNodeBuilder {
        private RegexNode left;
        private RegexNode right;

        private RegexConcatNodeBuilder() {
        }

        public static RegexConcatNodeBuilder aRegexConcatNode() {
            return new RegexConcatNodeBuilder();
        }

        public RegexConcatNodeBuilder withLeft(RegexNode left) {
            this.left = left;
            return this;
        }

        public RegexConcatNodeBuilder withRight(RegexNode right) {
            this.right = right;
            return this;
        }

        public RegexConcatNode build() {
            RegexConcatNode regexConcatNode = new RegexConcatNode();
            regexConcatNode.setLeft(left);
            regexConcatNode.setRight(right);
            return regexConcatNode;
        }
    }
}

