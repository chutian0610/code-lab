package info.victorchu.j11.type.classload;

import org.junit.jupiter.api.Test;

/**
 * jdk 11 下面检查 ClassLoader
 */
public class ClassLoaderTest {
    @Test
    public void testPrintClassLoaderPaths() {
        // 启动 classloader path
        System.out.println(System.getProperty("sun.boot.class.path"));
        // 扩展classloader
        System.out.println(System.getProperty("java.ext.dirs"));
        // 应用classloader
        System.out.println(System.getProperty("java.class.path"));
    }

    @Test
    public void simplyGetClassLoaders(){
        ClassLoader classLoader = this.getClass().getClassLoader();
        // jdk.internal.loader.ClassLoaders$AppClassLoader
        System.out.println(classLoader);
        ClassLoader parent = classLoader.getParent();
        //jdk.internal.loader.ClassLoaders$PlatformClassLoader
        System.out.println(parent);
        ClassLoader parent2 = parent.getParent();
        //null
        System.out.println(parent2);
    }
}
