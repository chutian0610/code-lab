package info.victorchu.jdk.lab.jvm.oom;

import java.util.HashSet;
import java.util.Set;

/**
 * JDK1.6
 * VM Option: -XX:PermSize=1M -XX:MaxPerSize=1M
 * JDK1.7+
 * VM Option: -Xms20m -Xmx20m
 */

public class RuntimeConstantPoolOOM {
    
    public static void main(String[] args) {
        // 使用Set保持着常量池引用，避免Full GC回收常量池行为
        Set<String> set = new HashSet<String>();
        short i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}