package info.victorchu.toy.compiler.simpleregex.ast;

/**
 * RegexNode 的抽象访问者
 *
 * @author victorchutian
 */
public interface RegexExpressionVisitor<T, C>
{

    // --------------- 基于 RegexNode 具体类型分发 ------------------------

    T visitChar(CharExpression node, C context);

    T visitConcat(ConcatExpression node, C context);

    T visitOr(OrExpression node, C context);

    T visitRepeat(RepeatExpression node, C context);

    // 泛型入口
    default T process(RegexExpression node, C context)
    {
        return node.accept(this, context);
    }
}
