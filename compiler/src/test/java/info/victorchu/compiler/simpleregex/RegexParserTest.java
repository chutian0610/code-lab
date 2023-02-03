package info.victorchu.compiler.simpleregex;

import info.victorchu.compiler.simpleregex.ast.RegexNode;
import info.victorchu.compiler.simpleregex.ast.RegexParser;
import info.victorchu.compiler.simpleregex.util.RegexNodePrintVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RegexParserTest {
    @Test
    void parse() throws IOException {
        RegexNode regexNode= RegexParser.parse("ab*c|bc");
        RegexNodePrintVisitor visitor = new RegexNodePrintVisitor();
        regexNode.accept(visitor);
        System.out.println(visitor.build());
    }
}