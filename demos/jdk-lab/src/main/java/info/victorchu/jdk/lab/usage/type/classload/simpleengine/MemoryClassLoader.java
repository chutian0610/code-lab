package info.victorchu.jdk.lab.usage.type.classload.simpleengine;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 内存数据，classloader
 */
public class MemoryClassLoader extends URLClassLoader {

    // class name to class bytes:
    Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

    public MemoryClassLoader(Map<String, byte[]> classBytes) {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
        this.classBytes.putAll(classBytes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }

}