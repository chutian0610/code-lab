package info.victorchu.compiler.regexer;

/**
 * Regex AST Node 的抽象父类
 * @Description:
 * @Date:2022/2/11 5:17 下午
 * @Author:victorchutian
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
        REGEX_OR,                 // s|t 或
        REGEX_CONCAT,             // st 连接
        REGEXP_REPEAT,            // s*重复
        REGEXP_CHAR               // 字符
    }

    /**
     * 访问者模式
     * @param visitor
     */
    public abstract void accept(AbstractVisitor visitor);
}

