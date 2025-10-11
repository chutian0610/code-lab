package info.victorchu.snippets.concurrency.util;

import com.google.common.base.Preconditions;
import jdk.internal.joptsimple.internal.Strings;

import java.io.Closeable;

public class SetThreadName
        implements Closeable
{
    private final String originalThreadName;

    public SetThreadName(String name)
    {
        Preconditions.checkState(!Strings.isNullOrEmpty(name), "thread name is null");
        originalThreadName = Thread.currentThread().getName();
        Thread.currentThread().setName(name + "-" + Thread.currentThread().getId());
    }

    @Override
    public void close()
    {
        Thread.currentThread().setName(originalThreadName);
    }
}
