package info.victorchu.spi;

/**
 * spi 接口
 */
public interface Search {
    Boolean canSearch(String query);
    String search(String query);
}
