package info.victorchu.snippets.clazz;

import info.victorchu.snippets.clazz.ClassScanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author victorchu
 * @date 2022/8/5 21:44
 */
class ClassScannerTest {

    @Test
    public  void testScan4Package() throws IOException {
        List<String> list=  ClassScanner.scanForPackage("info.victorchu.snippets.clazz",true);
        System.out.println(list);
        Assertions.assertTrue(list.size()>0);

    }
}