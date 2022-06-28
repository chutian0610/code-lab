package info.victorchu.compiler.simpleregex.util;

import info.victorchu.compiler.simpleregex.*;

import java.util.Stack;

public class RegexNodePrintVisitor implements RegexNodeVisitor<Object> {
    private final Stack<Integer> count = new Stack<>();
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

        if(count.peek() == 1){
            count.pop();
            depth--;
        }else {
            int tmp = count.pop();
            count.push(tmp-1);
        }
        return null;
    }

    @Override
    public Object visit(RegexConcatNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Concat]").append("\n");
        depth++;
        count.add(2);
        visit(node.getLeft());
        visit(node.getRight());
        return null;
    }

    @Override
    public Object visit(RegexOrNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Or]").append("\n");
        depth++;
        count.add(2);
        visit(node.getLeft());
        visit(node.getRight());
        return null;
    }

    @Override
    public Object visit(RegexRepeatNode node) {
        for (int i=0;i<depth;i++) {
            stringBuilder.append(tab);
        }
        stringBuilder.append(prefix0+"Node[Repeat]").append("\n");
        depth++;
        count.add(1);
        visit(node.getInnerNode());
        return null;
    }

    public String build(){
        return stringBuilder.toString();
    }
}
