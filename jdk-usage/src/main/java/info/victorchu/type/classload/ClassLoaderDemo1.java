package info.victorchu.type.classload;

/**
 * 用于验证ClassLoader.loadClass的逻辑。
 * @see info.victorchu.type.classload.ClassLoaderDemo
 */
public class ClassLoaderDemo1 {
    public static void main(String [] args){
        try {
            ClassLoader system = ClassLoader.getSystemClassLoader();
            //没有执行静态代码块，由此可见DemoClass只是进行了装载，没有进行链接与初始化。
            System.out.println("----------方法4----------");
            Class<DemoClass> cls  = (Class<DemoClass>)ClassLoader.getSystemClassLoader().loadClass("info.victorchu.type.classload.DemoClass");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
