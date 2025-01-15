package info.victorchu.jdk.lab.usage.socket.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static info.victorchu.jdk.lab.usage.socket.Constant.PORT;

public class EchoServer
        implements Runnable
{
    private final ExecutorService executor;
    public EchoServer()
    {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    public static void main(String[] args)
    {
        new EchoServer().run();
    }
    @Override
    public void run()
    {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                try {
                    System.out.println("Waiting for client...");
                    // 阻塞
                    Socket socket = ss.accept();
                    System.out.println("New client connected: " + socket.getRemoteSocketAddress());
                    executor.submit(() -> {
                        new EchoHandler(socket).handle();
                    });
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            // ignored
            e.printStackTrace();
        }
    }
}