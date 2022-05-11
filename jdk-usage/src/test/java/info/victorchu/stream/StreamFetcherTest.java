package info.victorchu.stream;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class StreamFetcherTest {

    @Test
    void fromCollection() {
//        List<String> list= Arrays.asList("a","b","c");
//        Assertions.assertDoesNotThrow(()->StreamFetcher.fromCollection(list,false));
        int a = 1;
        int[] as = new int[1];
        Class clazz = int.class;
        System.out.println(clazz);
        Class clazz2 = as.getClass().getComponentType();
        System.out.println(clazz2.isPrimitive());
        System.out.println(clazz2.getName());
    }
}