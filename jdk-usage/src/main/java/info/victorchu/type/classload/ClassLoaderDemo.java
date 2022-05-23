package info.victorchu.type.classload;

public class ClassLoaderDemo {
    private String name;

    private static boolean flag;
    static {
        flag = false;
        System.out.println("flag 的值为：" + flag);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static void main(String [] args){
        try {
            ClassLoader system = ClassLoader.getSystemClassLoader();
            Class<ClassLoaderDemo> cls = null;
            System.out.println("----------方法1----------");
            cls = (Class<ClassLoaderDemo>)Class.forName("info.victorchu.type.classload.ClassLoaderDemo");

            System.out.println("----------方法2----------");
            cls = (Class<ClassLoaderDemo>)Class.forName("info.victorchu.type.classload.ClassLoaderDemo", false, system);

            // 类加载过程中的缓存机制，由于方法1已经加载了该类，因此方法3不会再次加载该类
            System.out.println("----------方法3----------");
            cls = (Class<ClassLoaderDemo>)Class.forName("info.victorchu.type.classload.ClassLoaderDemo", true, system);

            //没有执行静态代码块，由此可见Config只是进行了装载，没有进行链接与初始化。
            System.out.println("----------方法4----------");
            cls = (Class<ClassLoaderDemo>)ClassLoader.getSystemClassLoader().loadClass("info.victorchu.type.classload.ClassLoaderDemo");


        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
