package info.victorchu.study.regex;

/**
 * 字符节点
 * @Description:
 * @Date:2022/2/11 5:18 下午
 * @Author:victorchutian
 */
public class RegexCharNode extends RegexNode {
    public RegexCharNode() {
        super(NodeType.REGEXP_CHAR);
    }

    private Character character;

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public void accept(AbstractVisitor visitor) {
        visitor.visit(this);
    }


    public static final class RegexCharNodeBuilder {
        private Character character;

        private RegexCharNodeBuilder() {
        }

        public static RegexCharNodeBuilder aRegexCharNode() {
            return new RegexCharNodeBuilder();
        }

        public RegexCharNodeBuilder withCharacter(Character character) {
            this.character = character;
            return this;
        }

        public RegexCharNode build() {
            RegexCharNode regexCharNode = new RegexCharNode();
            regexCharNode.setCharacter(character);
            return regexCharNode;
        }
    }
}

