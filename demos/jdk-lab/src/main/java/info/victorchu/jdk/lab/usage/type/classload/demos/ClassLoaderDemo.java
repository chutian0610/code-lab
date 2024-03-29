package info.victorchu.jdk.lab.usage.type.classload.demos;

/**
 * 用于验证Class.forName的逻辑。
 * @see ClassLoaderDemo1
 */
public class ClassLoaderDemo {


    public static void main(String [] args){
        try {
            ClassLoader system = ClassLoader.getSystemClassLoader();
            Class<DemoClass> cls = null;
            System.out.println("----------方法1----------");
            cls = (Class<DemoClass>) Class.forName("info.victorchu.jdk.lab.usage.type.classload.demos.DemoClass");

            System.out.println("----------方法2----------");
            cls = (Class<DemoClass>) Class.forName("info.victorchu.jdk.lab.usage.type.classload.demos.DemoClass", false, system);

            // 类加载过程中的缓存机制，由于方法1已经加载了该类，因此方法3不会再次加载该类
            System.out.println("----------方法3----------");
            cls = (Class<DemoClass>) Class.forName("info.victorchu.jdk.lab.usage.type.classload.demos.DemoClass", true, system);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
