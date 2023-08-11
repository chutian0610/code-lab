package info.victorchu.snippets.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @Author victor
 * @Email victorchu0610@outlook.com
 * @Version 1.0
 * @Description CycleBufferedFileReader 循环读取文件
 * 用于从测试文件构造无限流数据
 */
public class CycleBufferedFileReader implements Closeable {
    private File file;
    private BufferedReader bufferedReader;

    public CycleBufferedFileReader(File file) throws FileNotFoundException {
        this.file = file;
        bufferedReader = new BufferedReader(new FileReader(file));
    }

    public CycleBufferedFileReader(Path path) throws FileNotFoundException {
        this(path.toFile());
    }

    public int read() throws IOException {
        int result = bufferedReader.read();
        if (result == -1) {
            reset();
            result = bufferedReader.read();
            if (result == -1) {
                throw new IllegalArgumentException("file content must not empty!");
            }
        }
        return result;
    }

    public String readLine() throws IOException {
        String result = bufferedReader.readLine();
        if (result == null) {
            // reader meet eof
            reset();
            result = bufferedReader.readLine();
            if (result == null) {
                throw new IllegalArgumentException("file content must not empty!");
            }
        }
        return result;
    }

    private void reset() throws IOException {
        try {
            bufferedReader.close();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        bufferedReader = new BufferedReader(new FileReader(file));
    }

    @Override
    public void close() throws IOException {
        try {
            bufferedReader.close();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }
}