package info.victorchu.spi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DoSearchTest {

    @Test
    void search() {
        Assertions.assertEquals("文件搜索",DoSearch.search("file://search"));
    }
}