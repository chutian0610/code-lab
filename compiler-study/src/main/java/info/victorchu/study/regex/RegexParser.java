package info.victorchu.study.regex;

/**
 * 简单正则表达式parser.
 *
 * <pre>
 * bnf 语法，递归下降
 *
 * {@literal <regex>} ::= {@literal <unionexp>} '|' {@literal <regex>}
 *              | {@literal <unionexp>}
 * {@literal <unionexp>} ::= {@literal <concatexp>} {@literal <unionexp>}
 *              | {@literal <concatexp>}
 * {@literal <concatexp>} ::= {@literal <repeatexp>} '*'
 *              | &repeatexp>}
 * {@literal <repeatexp>} := {@literal <charexp>}
 *               | '(' {@literal <regexexp>} ')'
 * {@literal <charexp&gt; ::= {@literal <Unicode character>} | '\' {@literal <Unicode character>}
 * </pre>
 *
 * @Description: 将正则语法解析为AST Node
 * @Date:2022/2/11 5:06 下午
 * @Author:victorchutian
 */
public class RegexParser {
    private String regexStr;
    private int position;

    public static RegexNode parse(String str){
        return new RegexParser(str).parseRegex();
    }

    private RegexParser(String regexStr) {
        this.regexStr = regexStr;
    }

    private boolean matchChar(char c) {
        if (position >= regexStr.length())
            return false;
        if (regexStr.charAt(position) == c) {
            position++;
            return true;
        }
        return false;
    }

    private boolean notEnd() {
        return position < regexStr.length();
    }

    private boolean peek(String s) {
        return notEnd() && s.indexOf(regexStr.charAt(position)) != -1;
    }

    private char next() throws IllegalArgumentException {
        if (!notEnd())
            throw new IllegalArgumentException("unexpected end-of-string");
        return regexStr.charAt(position++);
    }

    private RegexNode parseRegex(){
        RegexNode regexNode = parseUnionExp();
        if(matchChar('|')){
            return RegexOrNode.RegexOrNodeBuilder.aRegexOrNode()
                    .withLeft(regexNode)
                    .withRight(parseUnionExp())
                    .build();
        }
        return regexNode;
    }


    private RegexNode parseUnionExp(){
        RegexNode regexNode = parseConcatExp();
        if( notEnd() && !peek("|)")){
            return RegexConcatNode.RegexConcatNodeBuilder.aRegexConcatNode()
                    .withLeft(regexNode)
                    .withRight(parseUnionExp())
                    .build();
        }
        return regexNode;
    }

    private RegexNode parseConcatExp(){
        RegexNode regexNode = parseRepeatExp();
        if(matchChar('*')){
            return RegexRepeatNode.RegexRepeatNodeBuilder.aRegexRepeatNode()
                    .withInnerNode(regexNode).build();
        }
        return regexNode;
    }
    private RegexNode parseRepeatExp(){
        if(matchChar('(')){
            RegexNode regex = parseRegex();
            if(matchChar(')')){
                return regex;
            }else {
                throw new IllegalArgumentException("expected ')' at position " + position);
            }
        }else {
            return parseCharExp();
        }
    }
    private RegexNode parseCharExp(){
        matchChar('\\');
        return RegexCharNode.RegexCharNodeBuilder.aRegexCharNode()
                .withCharacter(next())
                .build();
    }
}

