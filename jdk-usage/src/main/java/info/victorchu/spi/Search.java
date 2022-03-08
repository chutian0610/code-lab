package info.victorchu.spi;

public interface Search {
    Boolean canSearch(String query);
    String search(String query);
}
