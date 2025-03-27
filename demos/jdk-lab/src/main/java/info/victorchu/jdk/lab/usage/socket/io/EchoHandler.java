package info.victorchu.jdk.lab.usage.socket.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoHandler
{
    private final Socket socket;

    public EchoHandler(Socket socket)
    {
        this.socket = socket;
    }

    public void handle()
    {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String message;
            while (true) {
                if (in.ready()) {
                    message = in.readLine();
                    System.out.println("[" + Thread.currentThread() + "] Received: " + message);

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
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
