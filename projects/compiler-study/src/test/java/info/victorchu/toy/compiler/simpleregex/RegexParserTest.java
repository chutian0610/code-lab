package info.victorchu.toy.compiler.simpleregex;

import info.victorchu.toy.compiler.simpleregex.ast.RegexExpression;
import info.victorchu.toy.compiler.simpleregex.ast.RegexParser;
import info.victorchu.toy.compiler.simpleregex.ast.RegexExpressionTreePrinter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RegexParserTest {
    @Test
    void parse01() throws IOException {
        RegexExpression regexExpression = RegexParser.parse("ab*c|bc");
        RegexExpressionTreePrinter visitor = new RegexExpressionTreePrinter();
        System.out.println(visitor.build(regexExpression));
    }
    @Test
    void parse02() throws IOException {
        RegexExpression regexExpression = RegexParser.parse("(A|a)b*c|bc");
        RegexExpressionTreePrinter visitor = new RegexExpressionTreePrinter();
        System.out.println(visitor.build(regexExpression));
    }
}