package info.victorchu.compiler.simpleregex.ast;

/**
 * RegexNode 的抽象访问者
 * @date 2022/2/11 5:17 下午
 * @author victorchutian
 */
public interface RegexNodeVisitor<T,C> {

    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    T visitCharNode(RegexCharNode node, C context);
    T visitConcatNode(RegexConcatNode node,C context);
    T visitOrNode(RegexOrNode node,C context);
    T visitRepeatNode(RegexRepeatNode node,C context);

    // 泛型入口
    default T process(RegexNode node,C context){
        return node.accept(this,context);
    }

}
