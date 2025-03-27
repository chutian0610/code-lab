package info.victorchu.jdk.lab.usage.socket.nio;

import info.victorchu.jdk.lab.usage.socket.Constant;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class NIOServer
{
    public void initiateReactiveServer(int port) throws Exception {

        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.registerChannel(SelectionKey.OP_ACCEPT, server);

        dispatcher.registerEventHandler(
                SelectionKey.OP_ACCEPT,()-> new AcceptEventHandler(
                        dispatcher.getDemultiplexer()));

        dispatcher.registerEventHandler(
                SelectionKey.OP_READ, ()-> new ReadEventHandler(
                        dispatcher.getDemultiplexer()));

        dispatcher.registerEventHandler(
                SelectionKey.OP_WRITE, ()-> new WriteEventHandler(
                        dispatcher.getDemultiplexer()));

        dispatcher.run();

    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting NIO server at port : " + Constant.PORT);
        new NIOServer().
                initiateReactiveServer(Constant.PORT);
    }

}
