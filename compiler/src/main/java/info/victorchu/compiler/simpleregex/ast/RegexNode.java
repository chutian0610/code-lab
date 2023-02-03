package info.victorchu.compiler.simpleregex.ast;

/**
 * Regex AST Node 的抽象父类
 * @date 2022/2/11 5:17 下午
 * @author victorchutian
 */
public abstract class RegexNode {

    protected NodeType nodeType;

    public RegexNode(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * 节点类型
     */
    public static enum NodeType{
        /**
         * s|t 或
         */
        REGEX_OR,
        /**
         * st 连接
         */
        REGEX_CONCAT,
        /**
         * s* 重复
         */
        REGEXP_REPEAT,
        /**
         * 字符
         */
        REGEXP_CHAR
    }

    /**
     * 访问者模式, 子类实现具体的 accept 方法
     * @param visitor 访问者
     * @return 返回 T
     */
    public abstract <T> T accept(RegexNodeVisitor<T> visitor);
}

