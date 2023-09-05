package info.victorchu.toy.compiler.simpleregex.automata;

import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionTreePrinter;
import info.victorchu.toy.compiler.regex.ast.RegexParser;
import info.victorchu.toy.compiler.regex.automata.NFA;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author victorchu
 * @date 2023/9/5 16:26
 */
class NFATest
{
    @Test
    void parse01()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("ab");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));

        NFA nfa = NFA.buildNFA(regexExpression);
        System.out.println(nfa.print());
    }

    @Test
    void parse02()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a|b");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));

        NFA nfa = NFA.buildNFA(regexExpression);
        System.out.println(nfa.print());
    }

    @Test
    void parse03()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a*b");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));

        NFA nfa = NFA.buildNFA(regexExpression);
        System.out.println(nfa.print());
    }

    @Test
    void parse04()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("(a|b)*abb");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));

        NFA nfa = NFA.buildNFA(regexExpression);
        System.out.println(nfa.print());
    }
}