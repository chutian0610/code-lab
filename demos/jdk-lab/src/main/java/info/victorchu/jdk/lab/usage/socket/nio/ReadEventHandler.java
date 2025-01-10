package info.victorchu.jdk.lab.usage.socket.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ReadEventHandler implements EventHandler {

    private Selector demultiplexer;
    private ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

    public ReadEventHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
    }


    @Override
    public void handleEvent(SelectionKey handle) throws Exception {
        SocketChannel socketChannel =
                (SocketChannel) handle.channel();

        socketChannel.read(inputBuffer); // Read data from client
        inputBuffer.flip();
        while (inputBuffer.hasRemaining()) {
            int startPos = inputBuffer.position();
            boolean found = false;
            for (int i = inputBuffer.position(); i < inputBuffer.limit(); i++) {
                if (inputBuffer.get(i) == '\n') {
                    int len = i + 1 - startPos;
                    ByteBuffer tar = ByteBuffer.allocate(len);
                    for (int j = 0; j < len; j++) {
                        tar.put(inputBuffer.get());
                    }
                    tar.flip();
                    send(socketChannel,tar);
                    found = true;
                    break;
                }
            }
            if (!found) {
                inputBuffer.position(startPos);
                break;
            }
        }
        inputBuffer.compact();
    }

    private void send(SocketChannel socketChannel,ByteBuffer tar)
            throws Exception
    {
        int remaining = tar.remaining();

        // 提取字节数组
        byte[] bytes = new byte[remaining];
        tar.get(bytes);

        String message = new String(bytes);
        message = message.stripTrailing();
        System.out.println("Received : " + message);
        if ("bye".equalsIgnoreCase(message)) {
            System.out.println("客户端断开连接: " + socketChannel.getRemoteAddress());
            socketChannel.write(ByteBuffer.wrap("bye\n".getBytes()));
            socketChannel.close();
            return;
        }
        socketChannel.register(
                demultiplexer, SelectionKey.OP_WRITE, message+"\n");
    }

}
