package info.victorchu.compiler.simpleregex.util;

/**
 * pair with two unmodified elements
 * @param <L>
 * @param <R>
 * @author victorchu
 */
public class Pair <L,R>{
    public L getLeft() {
        return left;
    }

    private L left;

    public R getRight() {
        return right;
    }

    private R right;

    public static <L,R> Pair<L,R> of(L left,R right){
        return new Pair<>(left,right);
    }

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }
}
