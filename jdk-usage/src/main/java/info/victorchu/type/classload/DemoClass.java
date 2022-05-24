package info.victorchu.type.classload;

/**
 * 工具类。
 * @see info.victorchu.type.classload.ClassLoaderDemo
 */
public class DemoClass {
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
}
