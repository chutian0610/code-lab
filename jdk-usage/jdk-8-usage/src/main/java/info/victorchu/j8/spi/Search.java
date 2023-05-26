package info.victorchu.j8.spi;

/**
 * spi 接口
 */
public interface Search {
    Boolean canSearch(String query);
    String search(String query);
}
