package info.victorchu.jdk.lab.usage.socket.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static info.victorchu.jdk.lab.usage.socket.Constant.PORT;

public class EchoServer
        implements Runnable
{
    public static void main(String[] args)
    {
        new EchoServer().run();
    }

    @Override
    public void run()
    {
        Handler handler = new Handler();
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                handler.handle(ss.accept());
            }
        }
        catch (IOException e) {
            // ignored
            e.printStackTrace();
        }
    }

    static class Handler
    {

        public void handle(Socket socket)
        {
            long lastHeartbeat = System.currentTimeMillis();
            long heartbeatInterval = 5000; // 心跳包间隔5秒
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                String message;
                while (true) {
                    if (in.ready()) {
                        message = in.readLine();
                        lastHeartbeat = System.currentTimeMillis();
                        System.out.println("["+Thread.currentThread().getName()+"] Received: " + message);

                        // 处理心跳包
                        if ("heartbeat".equalsIgnoreCase(message)) {
                            out.println("heartbeat_ack");
                            continue;
                        }

                        // 回应消息
                        out.println(message);

                        // 退出条件
                        if ("bye".equalsIgnoreCase(message)) {
                            System.out.println("客户端断开连接: " + socket.getInetAddress());
                            break;
                        }
                    } else {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastHeartbeat > 2 * heartbeatInterval) {
                            System.out.println("心跳包超时，断开连接: " + socket.getInetAddress());
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}