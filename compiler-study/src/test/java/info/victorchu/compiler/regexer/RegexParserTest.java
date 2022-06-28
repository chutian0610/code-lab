package info.victorchu.compiler.regexer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RegexParserTest {

    @Test
    void parse() throws IOException {
        RegexNode regexNode= RegexParser.parse("ab*c|bc");
        RegexNodeDrawVisitor visitor = new RegexNodeDrawVisitor();
        regexNode.accept(visitor);
        visitor.draw();
    }
}