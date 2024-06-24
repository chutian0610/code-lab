package info.victorchu.snippets.compile.pratt;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author victorchu
 */
public class Lexer
{
    LinkedList<Token> tokenList;

    public Lexer(String expr)
    {
        tokenList = expr.codePoints().mapToObj(c -> (char) c)
                .filter(x -> !Character.isWhitespace(x))
                .map(x -> {
                    if ("0123456789".indexOf(x) != -1) {
                        return new Token(Token.Type.Atom, x);
                    }
                    else {
                        return new Token(Token.Type.Operator, x);
                    }
                })
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
        tokenList.add(Token.EOF);
    }

    public Token next()
    {
        return tokenList.pop();
    }

    public Token peek()
    {
        return tokenList.peek();
    }
}
