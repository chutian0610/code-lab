package info.victorchu.compiler.regexer;

/**
 * RegexNode 的抽象访问者
 * @Description:
 * @Date:2022/2/11 5:17 下午
 * @Author:victorchutian
 */
public abstract class AbstractVisitor {

    public void visit(RegexNode node){
        node.accept(this);
    }
    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    public abstract void visit(RegexCharNode node);
    public abstract void visit(RegexConcatNode node);
    public abstract void visit(RegexOrNode node);
    public abstract void visit(RegexRepeatNode node);
}
