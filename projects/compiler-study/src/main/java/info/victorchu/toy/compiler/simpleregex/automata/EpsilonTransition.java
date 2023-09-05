package info.victorchu.toy.compiler.simpleregex.automata;

/**
 * @author victorchu
 * @date 2023/9/5 15:13
 */
public enum EpsilonTransition
        implements Transition
{
    INSTANCE;

    @Override
    public String toString()
    {
        return "ϵ";
    }
}
