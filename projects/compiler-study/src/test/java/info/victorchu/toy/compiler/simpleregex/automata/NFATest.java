package info.victorchu.toy.compiler.simpleregex.automata;

import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionTreePrinter;
import info.victorchu.toy.compiler.regex.ast.RegexParser;
import info.victorchu.toy.compiler.regex.automata.NFA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author victorchu
 * @date 2023/9/5 16:26
 */
class NFATest
{
    private static final Logger log = LoggerFactory.getLogger(NFATest.class);
    @Test
    void parse01()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("ab");
        String tree = RegexExpressionTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}======================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.print();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals("flowchart LR\n" +
                "s_0(0)-->|'a'|s_1(1)\n" +
                "s_1(1)-->|ϵ|s_2(2)\n" +
                "s_2(2)-->|'b'|s_3((3))\n", nfaStr);
    }

    @Test
    void parse02()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a|b");
        String tree = RegexExpressionTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}======================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.print();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals("flowchart LR\n" +
                "s_0(0)-->|ϵ|s_1(1)\n" +
                "s_1(1)-->|'a'|s_2(2)\n" +
                "s_2(2)-->|ϵ|s_5((5))\n" +
                "s_0(0)-->|ϵ|s_3(3)\n" +
                "s_3(3)-->|'b'|s_4(4)\n" +
                "s_4(4)-->|ϵ|s_5((5))\n", nfaStr);
    }

    @Test
    void parse03()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("a*b");
        String tree = RegexExpressionTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}======================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.print();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals("flowchart LR\n" +
                "s_0(0)-->|ϵ|s_1(1)\n" +
                "s_1(1)-->|'a'|s_2(2)\n" +
                "s_2(2)-->|ϵ|s_3(3)\n" +
                "s_3(3)-->|ϵ|s_4(4)\n" +
                "s_4(4)-->|'b'|s_5((5))\n" +
                "s_0(0)-->|ϵ|s_3(3)\n", nfaStr);
    }

    @Test
    void parse04()
            throws IOException
    {
        RegexExpression regexExpression = RegexParser.parse("(a|b)*abb");
        String tree = RegexExpressionTreePrinter.print(regexExpression);
        log.info("\n================== tree ================\n{}======================================", tree);
        NFA nfa = NFA.buildNFA(regexExpression);
        String nfaStr = nfa.print();
        log.info("\n================== NFA ================\n{}======================================", nfaStr);
        Assertions.assertEquals("flowchart LR\n" +
                "s_0(0)-->|ϵ|s_1(1)\n" +
                "s_1(1)-->|ϵ|s_2(2)\n" +
                "s_2(2)-->|'a'|s_3(3)\n" +
                "s_3(3)-->|ϵ|s_6(6)\n" +
                "s_6(6)-->|ϵ|s_7(7)\n" +
                "s_7(7)-->|ϵ|s_8(8)\n" +
                "s_8(8)-->|'a'|s_9(9)\n" +
                "s_9(9)-->|ϵ|s_10(10)\n" +
                "s_10(10)-->|'b'|s_11(11)\n" +
                "s_11(11)-->|ϵ|s_12(12)\n" +
                "s_12(12)-->|'b'|s_13((13))\n" +
                "s_1(1)-->|ϵ|s_4(4)\n" +
                "s_4(4)-->|'b'|s_5(5)\n" +
                "s_5(5)-->|ϵ|s_6(6)\n" +
                "s_0(0)-->|ϵ|s_7(7)\n", nfaStr);
    }
}