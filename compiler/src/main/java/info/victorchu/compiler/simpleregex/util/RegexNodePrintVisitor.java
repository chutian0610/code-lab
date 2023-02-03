package info.victorchu.compiler.simpleregex.util;

import info.victorchu.compiler.simpleregex.ast.*;

/**
 * print regex node tree in terminal
 * @author victorchu
 */
public class RegexNodePrintVisitor implements RegexNodeVisitor<Object,Void> {
    private Integer depth =0;
    private String tab = "    ";
    private String prefix0 = "+---";
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public Object visitCharNode(RegexCharNode node,Void context) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Char]("+node.getCharacter()+")").append("\n");
        return null;
    }

    @Override
    public Object visitConcatNode(RegexConcatNode node,Void context) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Concat]").append("\n");
        depth++;
        process(node.getLeft(),context);
        process(node.getRight(),context);
        depth--;
        return null;
    }

    @Override
    public Object visitOrNode(RegexOrNode node,Void context) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Or]").append("\n");
        depth++;
        process(node.getLeft(),context);
        process(node.getRight(),context);
        depth--;
        return null;
    }

    @Override
    public Object visitRepeatNode(RegexRepeatNode node,Void context) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Repeat]").append("\n");
        depth++;
        process(node.getInnerNode(),context);
        depth--;
        return null;
    }

    public String build(RegexNode node){
        this.process(node,null);
        return stringBuilder.toString();
    }
}
