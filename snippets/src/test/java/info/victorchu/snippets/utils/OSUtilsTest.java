package info.victorchu.snippets.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static info.victorchu.snippets.utils.OSUtils.getOSname;

/**
 * @author victorchu
 * @date 2022/8/5 21:55
 */
class OSUtilsTest {

    @Test
    public void test() {
        Assertions.assertDoesNotThrow(()->{
            System.out.println(getOSname());
            System.out.println(System.getProperty("os.name").toLowerCase());
        });
    }
}