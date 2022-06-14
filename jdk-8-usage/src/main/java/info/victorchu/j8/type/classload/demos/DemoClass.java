package info.victorchu.j8.type.classload.demos;

/**
 * 工具类。
 * @see ClassLoaderDemo
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
