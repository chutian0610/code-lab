package info.victorchu.snippets.concurrency.threadpool.shared;

import info.victorchu.snippets.concurrency.threadpool.priority.Prioritized;

public interface ResourceGroupManaged extends Prioritized {
    String getResourceGroupId();
}
