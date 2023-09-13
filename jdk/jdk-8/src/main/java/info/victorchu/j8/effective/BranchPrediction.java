package info.victorchu.j8.effective;

import java.util.Arrays;
import java.util.Random;

/**
 * @author victorchu
 * @date 2023/9/13 11:26
 */
public class BranchPrediction
{
    public static int[] generateData(){
        // Generate data
        int arraySize = 32768;
        int[] data = new int[arraySize];

        Random rnd = new Random(0);
        for (int c = 0; c < arraySize; ++c)
            data[c] = rnd.nextInt() % 256;
        return data;
    }
    public static int[] generateSortedData(){
        // Generate data
        int[] data = generateData();
        Arrays.sort(data);
        return data;
    }
    public static void main(String[] args)
    {
        int[] data = generateData();
        int[] sortedData = generateSortedData();
        int sum1 = 0;
        long total1 =0L;
        long flag1 = System.nanoTime();
        for (int i = 0; i < 100000; ++i)
        {
            for (int datum : data) {
                if (datum >= 128)
                    sum1 += datum;
            }
        }
        long flag2 = System.nanoTime();
        total1 +=flag2-flag1;

        int sum2 = 0;
        long total2 =0L;
        long flag11 = System.nanoTime();
        for (int i = 0; i < 100000; ++i)
        {
            for (int sortedDatum : sortedData) {
                if (sortedDatum >= 128)
                    sum2 += sortedDatum;
            }
        }
        long flag22 = System.nanoTime();
        total2 +=flag22-flag11;
        System.out.println("exec unsorted array cost: "+ (total1 / 1000000000.0) +"s,result= " + sum1);
        System.out.println("exec sorted array cost: "+(total2 / 1000000000.0) +"s,result= " + sum2);
    }
}
