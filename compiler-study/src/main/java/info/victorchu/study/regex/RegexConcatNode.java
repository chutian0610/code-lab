package info.victorchu.study.regex;

/**
 * 连接节点.
 * @Description:
 * @Date:2022/2/11 5:18 下午
 * @Author:victorchutian
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
    public void accept(AbstractVisitor visitor) {
        visitor.visit(this);
    }
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

