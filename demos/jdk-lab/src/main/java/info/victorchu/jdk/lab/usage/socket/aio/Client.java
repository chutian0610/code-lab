package info.victorchu.jdk.lab.usage.socket.aio;

import info.victorchu.jdk.lab.usage.socket.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", Constant.PORT);) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.print("Hello Proactor!");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String reply = in.readLine();
            System.out.println("Server responded: " + reply);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
