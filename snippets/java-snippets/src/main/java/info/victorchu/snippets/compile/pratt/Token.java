package info.victorchu.snippets.compile.pratt;

/**
 * @author victorchu
 */
public class Token
{
    public static Token EOF = new Token(Type.EOF, (char) 0);
    public Type type;
    public Character text;

    public Token(Type type, Character text)
    {
        this.type = type;
        this.text = text;
    }

    public boolean notEOF()
    {
        return type != Type.EOF;
    }

    @Override
    public String toString()
    {
        return "Token{" +
                "type=" + type +
                ", text=" + text +
                '}';
    }

    public static enum Type
    {
        Atom, Operator, EOF
    }
}
