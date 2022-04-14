package info.victorchu.mermaidjsjava.flow;

import lombok.Getter;

@Getter
public enum ArrowType{
    None("", ""),
    Fork("x","x"),
    Circle("o","o"),
    Arrow("<",">");
    private String left;
    private String right;

    ArrowType(String left, String right) {
        this.left = left;
        this.right = right;
    }
}