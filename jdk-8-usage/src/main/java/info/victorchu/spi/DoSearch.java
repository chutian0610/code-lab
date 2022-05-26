package info.victorchu.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * SPI 使用演示
 */
public class DoSearch {
    public static String search(String query) {
        ServiceLoader<Search> sl = ServiceLoader.load(Search.class);
        Iterator<Search> s = sl.iterator();
        while (s.hasNext()) {
            Search ss = s.next();
            if(ss.canSearch(query)) {
                return ss.search(query);
            }
        }
        throw new RuntimeException("No Search Impl Found");

    }
}
