package info.victorchu.snippets.clazz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author victorchu
 */
class ClassScannerTest
{

    @Test
    public void testScan4Package()
            throws IOException
    {
        List<String> list = ClassScanner.scanForPackage("info.victorchu.snippets.clazz", true);
        System.out.println(list);
        Assertions.assertTrue(!list.isEmpty());
    }
}