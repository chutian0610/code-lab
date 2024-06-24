package info.victorchu.snippets.tailrec;

/**
 * @author victorchu
 */
public class Fibonacci
{
    public static class FibonacciResult{
        public Long pre;
        public Long current;

        public Integer index;

        public FibonacciResult(Long pre, Long current,Integer index)
        {
            this.pre = pre;
            this.current = current;
            this.index = index;
        }

        public static FibonacciResult of(Long pre,Long current,Integer index){
            return new FibonacciResult(pre, current,index);
        }
    }
    public static TailRecursion<Integer,FibonacciResult> fibonacci(Integer n,FibonacciResult r){
        if(n ==0){
            return TailRecursion.done(FibonacciResult.of(-1L,0L,0));
        }
        else if (n ==1) {
            return TailRecursion.done(FibonacciResult.of(0L,1L,1));
        }else {
            if(n>r.index){
                return ()->fibonacci(n, FibonacciResult.of(r.current, r.pre +r.current,++r.index));
            }else {
                // n=r.index
                return TailRecursion.done(FibonacciResult.of(r.pre,r.current,r.index));
            }
        }
    }

    public static Long fibonacci(Integer n){
        return fibonacci(n,FibonacciResult.of(0L,1L,1)).invoke().current;
    }

    public static void main(String[] args)
    {
        System.out.println(fibonacci(8));
    }
}
