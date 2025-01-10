package info.victorchu.jdk.lab.usage.socket.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class WriteEventHandler implements EventHandler {

    private Selector demultiplexer;
    public WriteEventHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
    }
    @Override
    public void handleEvent(SelectionKey handle) throws Exception {
        SocketChannel socketChannel =
                (SocketChannel) handle.channel();
        String message = (String) handle.attachment();
        socketChannel.write(ByteBuffer.wrap(message.getBytes()));
        socketChannel.register(
                demultiplexer, SelectionKey.OP_READ);
    }
}