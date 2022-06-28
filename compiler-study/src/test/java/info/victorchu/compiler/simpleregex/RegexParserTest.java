package info.victorchu.compiler.simpleregex;

import info.victorchu.compiler.simpleregex.util.RegexNodeDrawVisitor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RegexParserTest {

    @Test
    void parse() throws IOException {
        RegexNode regexNode= RegexParser.parse("ab*c|bc");
        RegexNodeDrawVisitor visitor = new RegexNodeDrawVisitor();
        regexNode.accept(visitor);
        visitor.draw();
    }
}