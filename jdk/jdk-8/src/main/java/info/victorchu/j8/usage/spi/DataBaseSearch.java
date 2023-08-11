package info.victorchu.j8.usage.spi;

public class DataBaseSearch implements Search{
    @Override
    public Boolean canSearch(String query) {
        return query.startsWith("db://");
    }

    @Override
    public String search(String query) {
        return "数据库搜索";
    }
}
