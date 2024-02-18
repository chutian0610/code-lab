package info.victorchu.jdk.lab.jvm.oom;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * VM Option: -Xmx10M -XX:MaxDirectMemorySize=10M
 */
public class DirectMemoryOOM{
    private static final int size = 1024*1024;

    public static void main(String[] args)throws Exception{
        List<ByteBuffer> buffers= new ArrayList<>();
        while(true){
            ByteBuffer bb = ByteBuffer.allocateDirect(size);
            buffers.add(bb);
        }
    }
}
