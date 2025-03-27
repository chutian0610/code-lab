package info.victorchu.jdk.lab.usage.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NIOClient
{
    private final ByteBuffer sendBuffer=ByteBuffer.allocate(2048);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(2048);
    private Selector selector;
    private SocketChannel socketChannel;
    public NIOClient()throws IOException{
        // 创建SocketChannel
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); // 设置为非阻塞模式

        // 连接到服务器
        boolean isConnected = socketChannel.connect(new InetSocketAddress("localhost", 8092));
        if (!isConnected) {
            while (!socketChannel.finishConnect()) {
                // 等待连接完成
            }
        }
        System.out.println("已连接到服务器");

        // 创建Selector并注册
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void close()
            throws IOException
    {
        selector.close();
        socketChannel.close();
    }

    public static void main(String[] args)
            throws IOException
    {
        NIOClient client = new NIOClient();
        Thread receiver=new Thread(client::receiveFromUser);
        receiver.start();
        client.talk();
        receiver.interrupt();
        client.close();
    }
    private void receiveFromUser() {;
        try(Scanner scanner= new Scanner(System.in)){
            String msg;
            while (true) {
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                if(scanner.hasNext()) {
                    msg = scanner.nextLine();
                    if (msg != null) {
                        synchronized (sendBuffer) {
                            sendBuffer.put((msg + "\n").getBytes());
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void talk()throws IOException {
        out:
        while (true){
            selector.select(); // 阻塞直到有事件发生
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();

                if (key.isReadable()) {
                    if(!receive(key)){
                        break out;
                    }
                }
                if (key.isWritable()) {
                    send(key);
                }
            }

        }
    }
    private void send(SelectionKey key)throws IOException{
        SocketChannel socketChannel=(SocketChannel)key.channel();
        synchronized(sendBuffer){
            sendBuffer.flip(); //设置写
            boolean write = false;
            while(sendBuffer.hasRemaining()){
                write = true;
                socketChannel.write(sendBuffer);
            }
            if(write){
                System.out.println("发送成功");
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            sendBuffer.compact();
        }
    }
    private boolean receive(SelectionKey key)throws IOException{
        System.out.println("读取...");
        SocketChannel socketChannel=(SocketChannel)key.channel();
        socketChannel.read(receiveBuffer);
        receiveBuffer.flip();
        if(receiveBuffer.hasRemaining()) {
            String receiveData = StandardCharsets.UTF_8.decode(receiveBuffer).toString();
            System.out.println("receive server message:" + receiveData);
            if("bye".equalsIgnoreCase(receiveData.trim())){
                return false;
            }
        }
        socketChannel.register(selector,SelectionKey.OP_WRITE);
        receiveBuffer.clear();
        return true;
    }

}

