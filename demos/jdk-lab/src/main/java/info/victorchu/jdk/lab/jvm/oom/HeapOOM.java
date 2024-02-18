package info.victorchu.jdk.lab.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM option: -Xms20m -Xmx20m
 */
public class HeapOOM {
    static class OOMObject{
    }

    public static void main(String[] args){
        List<OOMObject> list = new ArrayList<OOMObject>();
        while(true){
            list.add(new OOMObject());
        }
    }
}
