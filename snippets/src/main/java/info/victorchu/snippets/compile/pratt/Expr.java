package info.victorchu.snippets.compile.pratt;

import java.util.List;

/**
 * @author victorchu
 */
public class Expr
{
    public Type type;
    public Expr[] exprs;
    public Character text;

    private Expr()
    {
    }

    public static Expr composite(Character text, Expr... exprs)
    {
        Expr expr = new Expr();
        expr.type = Type.Composite;
        expr.exprs = exprs;
        expr.text = text;
        return expr;
    }

    public static Expr value(Character text)
    {
        Expr expr = new Expr();
        expr.type = Type.Value;
        expr.text = text;
        return expr;
    }

    @Override
    public String toString()
    {
        if (type.equals(Type.Value)) {
            return text.toString();
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(text);
            for (Expr expr : exprs) {
                sb.append(" ").append(expr.toString());
            }
            sb.append(")");
            return sb.toString();
        }
    }

    public static enum Type
    {
        Value, Composite
    }
}
