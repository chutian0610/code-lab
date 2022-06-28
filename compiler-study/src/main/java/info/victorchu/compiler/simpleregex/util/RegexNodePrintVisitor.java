package info.victorchu.compiler.simpleregex.util;

import info.victorchu.compiler.simpleregex.*;

/**
 * print regex node tree in terminal
 * @author victorchu
 */
public class RegexNodePrintVisitor implements RegexNodeVisitor<Object> {
    private Integer depth =0;
    private String tab = "    ";
    private String prefix0 = "+---";
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public Object visit(RegexCharNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Char]("+node.getCharacter()+")").append("\n");
        return null;
    }

    @Override
    public Object visit(RegexConcatNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Concat]").append("\n");
        depth++;
        visit(node.getLeft());
        visit(node.getRight());
        depth--;
        return null;
    }

    @Override
    public Object visit(RegexOrNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Or]").append("\n");
        depth++;
        visit(node.getLeft());
        visit(node.getRight());
        depth--;
        return null;
    }

    @Override
    public Object visit(RegexRepeatNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Repeat]").append("\n");
        depth++;
        visit(node.getInnerNode());
        depth--;
        return null;
    }

    public String build(){
        return stringBuilder.toString();
    }
}
