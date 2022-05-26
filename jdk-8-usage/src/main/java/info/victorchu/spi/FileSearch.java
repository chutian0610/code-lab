package info.victorchu.spi;

public class FileSearch implements Search{
    @Override
    public Boolean canSearch(String query) {
        return query.startsWith("file://");
    }

    @Override
    public String search(String query) {
        return "文件搜索";
    }
}
