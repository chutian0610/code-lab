package info.victorchu.jdk.lab.usage.socket.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer bytesRead, ByteBuffer attachment) {
        if (bytesRead == -1) {
            try {
                socketChannel.close();
                System.out.println("客户端断开连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        attachment.flip();
        byte[] data = new byte[attachment.remaining()];
        attachment.get(data);;

        String message = new String(data);

        System.out.println("["+Thread.currentThread()+"] Received message from client : " + message);

        ByteBuffer outputBuffer = ByteBuffer.wrap(data);
        // Echo the message back to client
        WriteCompletionHandler writeCompletionHandler = new WriteCompletionHandler(socketChannel);
        socketChannel.write(outputBuffer,outputBuffer, writeCompletionHandler);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        // Handle read failure.....
    }

}
