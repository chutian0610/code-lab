package info.victorchu.snippets.compile.pratt;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import info.victorchu.snippets.utils.Pair;

/**
 * @author victorchu
 */
public class Parser
{
    private Lexer lexer;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
    }

    public Expr expr(int min_bp)
    {
        Expr lhs = exprFirst();
        Token token = null;
        while ((token = lexer.peek()).notEOF()) {
            if (token.type == Token.Type.Operator) {
                // 后缀一元操作符
                Pair<Integer, Integer> post = postfix_binding_power(token);
                if (post != null) {
                    if (post.getLeft() < min_bp) {
                        break;
                    }
                    lexer.next();
                    // 支持数组
                    if (token.text.equals('[')) {
                        Expr rhs = expr(0);
                        Token rSquare = lexer.next();
                        Preconditions.checkArgument(rSquare.text.equals(']') && rSquare.type.equals(Token.Type.Operator));
                        lhs = Expr.composite(token.text, lhs, rhs);
                    }
                    else {
                        lhs = Expr.composite(token.text, lhs);
                    }
                    continue;
                }
                // 二元操作符
                Pair<Integer, Integer> pair = infix_binding_power(token);
                if (pair != null) {
                    if (pair.getLeft() < min_bp) {
                        break;
                    }
                    lexer.next();
                    // 支持三目运算
                    if (token.text.equals('?')) {
                        Expr mhs = expr(0);
                        Token colon = lexer.next();
                        Preconditions.checkArgument(colon.text.equals(':') && colon.type.equals(Token.Type.Operator));
                        Expr rhs = expr(pair.getRight());
                        lhs = Expr.composite(token.text, lhs, mhs, rhs);
                    }
                    else {
                        Expr rhs = expr(pair.getRight());
                        lhs = Expr.composite(token.text, lhs, rhs);
                    }
                    continue;
                }
                break;
            }
            else {
                throw new RuntimeException("unexpected token:" + token);
            }
        }
        return lhs;
    }

    private Expr exprFirst()
    {
        Expr expr = null;
        Token token = lexer.next();
        if (token.type.equals(Token.Type.Atom)) {
            expr = Expr.value(token.text);
        }
        else if (token.type.equals(Token.Type.Operator)) {
            // 支持括号
            if (token.text.equals('(')) {
                expr = expr(0);
                Token rBracket = lexer.next();
                Preconditions.checkArgument(rBracket.text.equals(')') && rBracket.type.equals(Token.Type.Operator));
                return expr;
            }
            // 前缀一元操作符
            Pair<Integer, Integer> pair = prefix_binding_power(token);
            if (pair == null) {
                throw new RuntimeException("unexpected token" + token);
            }
            Expr rhs = expr(pair.getRight());
            expr = Expr.composite(token.text, rhs);
        }
        else {
            throw new RuntimeException("unexpected token" + token);
        }
        return expr;
    }

    static Map<Character, Pair<Integer, Integer>> infixBindingMap = new HashMap<>();

    static {
        infixBindingMap.put('+', Pair.of(5, 6));
        infixBindingMap.put('-', Pair.of(5, 6));
        infixBindingMap.put('*', Pair.of(7, 8));
        infixBindingMap.put('/', Pair.of(7, 8));
        // 左边更优先 => 右结合
        infixBindingMap.put('=', Pair.of(2, 1));
        infixBindingMap.put('?', Pair.of(4, 3));
    }

    private Pair<Integer, Integer> infix_binding_power(Token token)
    {
        Character op = token.text;
        return infixBindingMap.get(op);
    }

    static Map<Character, Pair<Integer, Integer>> prefixBindingMap = new HashMap<>();

    static {
        // null表示只能绑定右边
        prefixBindingMap.put('+', Pair.of(null, 9));
        prefixBindingMap.put('-', Pair.of(null, 9));
    }

    private Pair<Integer, Integer> prefix_binding_power(Token token)
    {
        Character op = token.text;
        return prefixBindingMap.get(op);
    }

    static Map<Character, Pair<Integer, Integer>> postfixBindingMap = new HashMap<>();

    static {
        postfixBindingMap.put('!', Pair.of(11, null));
        postfixBindingMap.put('[', Pair.of(11, null));
    }

    private Pair<Integer, Integer> postfix_binding_power(Token token)
    {
        Character op = token.text;
        return postfixBindingMap.get(op);
    }
}
