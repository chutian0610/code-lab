package info.victorchu.compiler.simpleregex.ast;

/**
 * RegexNode 的抽象访问者
 * @date 2022/2/11 5:17 下午
 * @author victorchutian
 */
public interface RegexNodeVisitor<T> {

    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    T visit(RegexCharNode node);
    T visit(RegexConcatNode node);
    T visit(RegexOrNode node);
    T visit(RegexRepeatNode node);

    default T  visit(RegexNode node){
        return node.accept(this);
    }
}
