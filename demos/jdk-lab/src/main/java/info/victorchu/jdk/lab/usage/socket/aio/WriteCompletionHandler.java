package info.victorchu.jdk.lab.usage.socket.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;

    public WriteCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer bytesWritten, ByteBuffer attachment) {
        // Handle write success...
        if (bytesWritten > 0) {
            byte[] buffer = new byte[bytesWritten];
            attachment.flip();
            // Rewind the input buffer to read from the beginning
            attachment.get(buffer);
            String message = new String(buffer);
            System.out.println("[" + Thread.currentThread() + "] send message to client : " + message);

            ByteBuffer readBuffer = ByteBuffer.allocate(2048);
            socketChannel.read(readBuffer, readBuffer, new ReadCompletionHandler(socketChannel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        // Handle write failure.....
    }

}
