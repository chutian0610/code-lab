package info.victorchu.type;

import org.junit.jupiter.api.Test;

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
       // sun.misc.Launcher$AppClassLoader
       System.out.println(classLoader);
       ClassLoader parent = classLoader.getParent();
       //sun.misc.Launcher$ExtClassLoader
       System.out.println(parent);
       ClassLoader parent2 = parent.getParent();
       //null
       System.out.println(parent2);
    }

    /**
     * -Djava.system.class.loader=info.victorchu.type.TestSystemClassLoader
     */
    @Test
    public void getSystemClassLoader(){
        // 默认是 sun.misc.Launcher$AppClassLoader
        System.out.println(ClassLoader.getSystemClassLoader());
        // 默认是null
        System.out.println(System.getProperty("java.system.class.loader"));
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
        ClassLoader classLoader = this.getClass().getClassLoader();
        System.out.println(classLoader);
    }
}
class TestSystemClassLoader extends ClassLoader{
    public TestSystemClassLoader(ClassLoader parent){
        super(parent);
    }
}
