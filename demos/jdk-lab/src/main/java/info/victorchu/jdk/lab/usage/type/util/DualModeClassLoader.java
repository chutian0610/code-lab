package info.victorchu.jdk.lab.usage.type.util;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * <pre>
 *  0. 并行加载
 *  1. parent first 模式。
 *  2. child first 模式。
 *      1. 支持部分class 是parent first
 * </pre>
 * 双模类加载器。

 */
public class DualModeClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final String[] alwaysParentFirstPatterns;

    private Boolean parentFirst;

    public DualModeClassLoader(URL[] urls, ClassLoader parent, String[] alwaysParentFirstPatterns, Boolean parentFirst) {
        super(urls, parent);
        this.alwaysParentFirstPatterns = alwaysParentFirstPatterns;
        this.parentFirst = parentFirst;
    }

    @Override
    public final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if(parentFirst){
          return loadClassParentFirst(name, resolve);
        }else{
          return loadClassChildFirst(name,resolve);
        }
    }
    public final Class<?> loadClassChildFirst(String name, boolean resolve) throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            // check whether the class should go parent-first
            for (String alwaysParentFirstPattern : alwaysParentFirstPatterns) {
                if (name.startsWith(alwaysParentFirstPattern)) {
                    return super.loadClass(name, resolve);
                }
            }
            try {
                // check the URLs
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                // let URLClassLoader do it, which will eventually call the parent
                c = super.loadClass(name, resolve);
            }
        } else if (resolve) {
            resolveClass(c);
        }

        return c;
    }


    public final Class<?> loadClassParentFirst(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }
}
