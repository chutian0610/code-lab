package info.victorchu.jdk.lab.usage.socket.aio;

import info.victorchu.jdk.lab.usage.socket.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class ProactorInitiator
{
    public static void main(String[] args) {
        try {
            System.out.println("Async server listening on port : " +
                    Constant.PORT);
            new ProactorInitiator().initiateProactiveServer(
                    Constant.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sleep indefinitely since otherwise the JVM would terminate
        while (true) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initiateProactiveServer(int port)
            throws IOException
    {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(
                        new InetSocketAddress(port));
        AcceptCompletionHandler acceptCompletionHandler =
                new AcceptCompletionHandler(listener);

        listener.accept(null, acceptCompletionHandler);
    }


}
