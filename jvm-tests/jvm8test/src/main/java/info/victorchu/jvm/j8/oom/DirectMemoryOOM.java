package info.victorchu.jvm.j8.oom;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import sun.misc.Unsafe;

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
