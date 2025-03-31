package info.victorchu.snippets.io.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetUtils
{
    private static int watchPort = Integer.getInteger("watch.port", 12306);

    private static ServerSocket ss;

    static {
        try {
            ss = new ServerSocket(watchPort);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 保证jvm不会退出，直到 telent shutdown
     */
    public static void guard()
    {
        System.out.println("I am started. You can `telnet localhost 12306` use `shutdown` to stop. ");
        try {
            while (true) {
                try {
                    Socket socket = ss.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (line.equals("shutdown")) {
                            System.out.println("shut down server.");
                            System.exit(0);
                            return;
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        finally {
            if (ss != null) {
                try {
                    ss.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * telnet上去后，运行run，能够执行预设任务
     */
    public static void run(Runnable runnable)
    {
        if (runnable != null) {
            try {
                while (true) {
                    try {
                        Socket socket = ss.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            if (line.equals("run")) {
                                try {
                                    runnable.run();
                                }
                                catch (Throwable ex) {
                                    ex.printStackTrace();
                                }
                            }
                            else if (line.equals("exit")) {
                                try {
                                    socket.close();
                                }
                                catch (Throwable ex) {
                                    ex.printStackTrace();
                                }
                            }
                            else if (line.equals("shutdown")) {
                                System.out.println("shut down client.");
                                System.exit(0);
                                return;
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            finally {
                if (ss != null) {
                    try {
                        ss.close();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}