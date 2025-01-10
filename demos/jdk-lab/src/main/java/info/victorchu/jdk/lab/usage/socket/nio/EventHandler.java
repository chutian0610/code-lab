package info.victorchu.jdk.lab.usage.socket.nio;

import java.nio.channels.SelectionKey;

public interface EventHandler {

    public void handleEvent(SelectionKey handle) throws Exception;

}





