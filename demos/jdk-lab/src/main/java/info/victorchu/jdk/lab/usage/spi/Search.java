package info.victorchu.jdk.lab.usage.spi;

/**
 * spi 接口
 */
public interface Search {
    Boolean canSearch(String query);
    String search(String query);
}
