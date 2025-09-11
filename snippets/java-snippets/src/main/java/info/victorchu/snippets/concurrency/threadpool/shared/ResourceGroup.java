package info.victorchu.snippets.concurrency.threadpool.shared;

import lombok.Getter;

public class ResourceGroup {
    @Getter
    private String name;
    @Getter
    private int queueSize;
    @Getter
    private int runningSize;


    public ResourceGroup(String name, int queueSize, int runningSize) {
        this.name = name;
        this.queueSize = queueSize;
        this.runningSize = runningSize;
    }
}
