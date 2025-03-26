package info.victorchu.jdk.lab.usage.socket.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
                Scanner scanner = new Scanner(System.in)) {
            channel.connect(new InetSocketAddress("127.0.0.1", 8092));
            System.out.println("[Client] 已连接到服务器");
            String userInput;
            while (true) {
                System.out.print("Request: ");
                userInput = scanner.nextLine();

                ByteBuffer buffer = ByteBuffer.allocate(2048);
                buffer.put(userInput.getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                channel.write(buffer);
                // 读取服务端返回的数据
                ByteBuffer readBuffer = ByteBuffer.allocate(2048);
                channel.read(readBuffer).get();
                readBuffer.flip();
                System.out.println("Response: " + new String(readBuffer.array(), 0, readBuffer.remaining()));
                if ("bye".equalsIgnoreCase(userInput)) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
