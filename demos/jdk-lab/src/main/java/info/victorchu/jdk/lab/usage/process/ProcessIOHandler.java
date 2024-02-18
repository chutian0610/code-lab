package info.victorchu.jdk.lab.usage.process;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * java 进程输出流处理，future模式
 * @Description:
 * @Date:2022/12/6 15:59
 * @Author:victorchutian
 */
public class ProcessIOHandler implements Callable<String> {

    InputStream is;
    StringBuffer output = new StringBuffer();

    ProcessIOHandler(InputStream is) {
        this.is = is;
    }

    @Override
    public String call() throws Exception {
        InputStreamReader std = new InputStreamReader(is);
        char[] buffer = new char[1024];
        boolean done = false;
        boolean stdclosed = false;
        while (!done) {
            boolean isLoop = false;
            if (!stdclosed){
                isLoop = true;
                int read = std.read(buffer, 0, buffer.length);
                if (read < 0) {
                    isLoop = true;
                    stdclosed = true;
                } else if (read > 0) {
                    isLoop = true;
                    output.append(buffer, 0, read);
                }
            }
            if (!isLoop) {
                done = true;
            }
        }
        return output.toString();
    }
}
