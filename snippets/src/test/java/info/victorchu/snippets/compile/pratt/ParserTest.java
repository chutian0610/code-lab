package info.victorchu.snippets.compile.pratt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author victorchu
 */
class ParserTest
{
    @Test
    void testParser01()
    {
        Parser parser = new Parser(new Lexer("1"));
        Assertions.assertEquals("1", parser.expr(0).toString());
    }

    @Test
    void testParser02()
    {
        Parser parser = new Parser(new Lexer("1 + 2 * 3 "));
        Assertions.assertEquals("(+ 1 (* 2 3))", parser.expr(0).toString());
    }

    @Test
    void testParser03()
    {
        Parser parser = new Parser(new Lexer("1 + 2 * 3 * 4 + 5 "));
        Assertions.assertEquals("(+ (+ 1 (* (* 2 3) 4)) 5)", parser.expr(0).toString());
    }

    @Test
    void testParser04()
    {
        Parser parser = new Parser(new Lexer("5 = 1 * 2 + 3 "));
        Assertions.assertEquals("(= 5 (+ (* 1 2) 3))", parser.expr(0).toString());
    }

    @Test
    void testParser05()
    {
        Parser parser = new Parser(new Lexer("--1 * 2"));
        Assertions.assertEquals("(* (- (- 1)) 2)", parser.expr(0).toString());
    }

    @Test
    void testParser06()
    {
        Parser parser = new Parser(new Lexer("-9!"));
        Assertions.assertEquals("(- (! 9))", parser.expr(0).toString());
    }

    @Test
    void testParser07()
    {
        Parser parser = new Parser(new Lexer("(((0)))"));
        Assertions.assertEquals("0", parser.expr(0).toString());
    }

    @Test
    void testParser08()
    {
        Parser parser = new Parser(new Lexer("2[0][1]"));
        Assertions.assertEquals("([ ([ 2 0) 1)", parser.expr(0).toString());
    }

    @Test
    void testParser09()
    {
        Parser parser = new Parser(new Lexer("1 ? 2 : 3 ? 4 : 5"));
        Assertions.assertEquals("(? 1 2 (? 3 4 5))", parser.expr(0).toString());
    }
}



