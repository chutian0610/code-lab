package info.victorchu.toy.compiler.simpleregex.automata;

import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionTreePrinter;
import info.victorchu.toy.compiler.regex.ast.RegexParser;
import info.victorchu.toy.compiler.regex.automata.DFA;
import info.victorchu.toy.compiler.regex.automata.NFA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author victorchu
 * @date 2023/9/14 19:33
 */
public class DFATest
{
    private static final Logger log = LoggerFactory.getLogger(NFATest.class);

    @Test
    void minimizationDFA01()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("ab");
        log.info("\n================== tree ================\n{}=====================================", RegexExpressionTreePrinter.print(regexExpression));
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'a'|ms_1(1)\n" +
                "ms_1(1)-->|'b'|ms_2((2))\n", minDfa.toString());
    }

    @Test
    void minimizationDFA02()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a|b");
        log.info("\n================== tree ================\n{}=====================================", RegexExpressionTreePrinter.print(regexExpression));
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'a'|ms_1((1))\n" +
                "ms_0(0)-->|'b'|ms_1((1))\n", minDfa.toString());
    }

    @Test
    void minimizationDFA03()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a*b");
        log.info("\n================== tree ================\n{}=====================================", RegexExpressionTreePrinter.print(regexExpression));
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
        Assertions.assertEquals("ms_0(0)-->|'b'|ms_2((2))\n" +
                "ms_0(0)-->|'a'|ms_0(0)\n", minDfa.toString());
    }

    @Test
    void minimizationDFA04()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("(a|b)*abb");
        log.info("\n================== tree ================\n{}=====================================", RegexExpressionTreePrinter.print(regexExpression));
        NFA nfa = NFA.buildNFA(regexExpression);
        log.info("\n================== NFA ================\n{}======================================", nfa);
        DFA dfa = nfa.toDFA();
        log.info("\n================== DFA ================\n{}======================================", dfa);
        log.info(dfa.printMapping());
        DFA minDfa = dfa.simplify();
        log.info("\n==================  mini DFA ================\n{}======================================", minDfa);
        log.info(minDfa.printMapping());
    }
}
