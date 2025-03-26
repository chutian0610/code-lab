package info.victorchu.jdk.lab.usage.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel listener;

    public AcceptCompletionHandler(AsynchronousServerSocketChannel listener) {
        this.listener = listener;
    }

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Void sessionState) {
        // handle this connection
        ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
        ReadCompletionHandler readCompletionHandler = new ReadCompletionHandler(socketChannel);
        socketChannel.read(inputBuffer, inputBuffer, readCompletionHandler);

        // Accept the next connection
        listener.accept(null, this);
    }

    @Override
    public void failed(Throwable exc,  Void sessionState) {
        // Handle connection failure...
    }

}