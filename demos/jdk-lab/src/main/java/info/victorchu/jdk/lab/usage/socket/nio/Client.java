package info.victorchu.jdk.lab.usage.socket.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static info.victorchu.jdk.lab.usage.socket.Constant.HOST;
import static info.victorchu.jdk.lab.usage.socket.Constant.PORT;

public class Client {
    public static void main(String[] args) {
        System.out.println("[Client] Started");
        try (Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {
            System.out.println("[Client] 已连接到服务器");
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
