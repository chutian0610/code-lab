package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * RegexNode 的抽象访问者
 *
 * @author victorchutian
 */
public interface RegexExpressionVisitor<T, C>
{

    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    T visitCharNode(CharExpression node, C context);

    T visitConcatNode(ConcatExpression node, C context);

    T visitOrNode(OrExpression node, C context);

    T visitRepeatNode(RepeatExpression node, C context);

    // 泛型入口
    default T process(RegexExpression node, C context)
    {
        return node.accept(this, context);
    }
}
