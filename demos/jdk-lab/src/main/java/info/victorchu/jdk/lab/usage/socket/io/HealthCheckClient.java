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

public class HealthCheckClient
{
    private volatile Socket socket;
    private volatile PrintWriter sOut;
    private volatile BufferedReader sIn;
    private ExecutorService executor;

    public HealthCheckClient()
            throws IOException
    {
        executor = Executors.newSingleThreadExecutor();
        createConnection();
        enableHealthCheck();
    }

    public void enableHealthCheck()
    {
        executor.submit(() -> {
            try {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    String response = communicate("heartbeat");
                    if (!"heartbeat_ack".equalsIgnoreCase(response)) {
                        System.out.println("心跳包回复异常: " + response);
                        break;
                    }
                    Thread.sleep(5000); // 心跳包间隔5秒
                }
            }
            catch (IOException e) {
                try {
                    // reconnect
                    createConnection();
                }
                catch (IOException ex) {
                    // 重连失败
                    throw new RuntimeException(ex);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized void createConnection()
            throws IOException
    {
        socket = new Socket(HOST, PORT);
        sOut = new PrintWriter(socket.getOutputStream(), true);
        sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socket.setKeepAlive(true);
    }

    public String handle(String userInput)
            throws IOException
    {
        sOut.println(userInput);
        return sIn.readLine();
    }

    public synchronized String communicate(String userInput)
            throws IOException
    {
        try {
            return handle(userInput);
        }
        catch (IOException e) {
            try {
                createConnection();
                //retry
                return handle(userInput);
            }
            catch (IOException ex) {
                throw ex;
            }
        }
    }

    public void close()
    {
        System.out.println("[Client] Closed");
        try {
            sIn.close();
            sOut.close();
            socket.close();
            executor.shutdown();
        }
        catch (IOException e) {

        }
    }

    public static void main(String[] args)
            throws IOException
    {
        System.out.println("[Client] Started");
        Scanner scanner = new Scanner(System.in);
        HealthCheckClient client = new HealthCheckClient();
        while (true) {
            System.out.print("Request: ");
            String userInput = scanner.nextLine();
            if ("bye".equalsIgnoreCase(userInput)) {
                System.out.println("Bye ~");
                client.close();
                scanner.close();
                break;
            }
            String resp = client.communicate(userInput);
            System.out.println("Response: " + resp);

        }
    }
}
