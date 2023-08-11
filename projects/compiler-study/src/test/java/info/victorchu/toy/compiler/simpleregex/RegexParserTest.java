package info.victorchu.toy.compiler.simpleregex;

import info.victorchu.toy.compiler.simpleregex.ast.RegexNode;
import info.victorchu.toy.compiler.simpleregex.ast.RegexParser;
import info.victorchu.toy.compiler.simpleregex.util.RegexNodePrintVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RegexParserTest {
    @Test
    void parse() throws IOException {
        RegexNode regexNode= RegexParser.parse("ab*c|bc");
        RegexNodePrintVisitor visitor = new RegexNodePrintVisitor();
        System.out.println(visitor.build(regexNode));
    }
}