package info.victorchu.study.regex;

/**
 * 或节点.
 * @Description:
 * @Date:2022/2/11 5:18 下午
 * @Author:victorchutian
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
    public void accept(AbstractVisitor visitor) {
        visitor.visit(this);
    }
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