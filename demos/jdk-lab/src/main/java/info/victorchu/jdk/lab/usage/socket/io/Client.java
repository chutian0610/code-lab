package info.victorchu.jdk.lab.usage.socket.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static info.victorchu.jdk.lab.usage.socket.Constant.HOST;
import static info.victorchu.jdk.lab.usage.socket.Constant.PORT;

public class Client
{
    public static void main(String[] args)
    {
        System.out.println("[Client] Started");
        try (Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
                ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            System.out.println("[Client] 已连接到服务器");
            executorService.submit(() -> {
                try {
                    while (true) {
                        if (Thread.currentThread().isInterrupted()){
                            break;
                        }
                        out.println("heartbeat");
                        String response = in.readLine();
                        if (!"heartbeat_ack".equalsIgnoreCase(response)) {
                            System.out.println("心跳包回复异常: "+response);
                            break;
                        }
                        Thread.sleep(5000); // 心跳包间隔5秒
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            String userInput;
            while (true) {
                System.out.print("Request: ");
                userInput = scanner.nextLine();
                out.println(userInput);

                String response = in.readLine();
                System.out.println("Response: "+response);

                if ("bye".equalsIgnoreCase(userInput)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
