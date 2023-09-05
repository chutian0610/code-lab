package info.victorchu.toy.compiler.simpleregex.ast;

import info.victorchu.toy.compiler.regex.ast.RegexExpression;
import info.victorchu.toy.compiler.regex.ast.RegexParser;
import info.victorchu.toy.compiler.regex.ast.RegexExpressionTreePrinter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RegexParserTest {
    @Test
    void parse01() throws IOException {
        RegexExpression regexExpression = RegexParser.parse("ab*c|bc");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));
    }
    @Test
    void parse02() throws IOException {
        RegexExpression regexExpression = RegexParser.parse("(A|a)b*c|bc");
        System.out.println(RegexExpressionTreePrinter.print(regexExpression));
    }
}