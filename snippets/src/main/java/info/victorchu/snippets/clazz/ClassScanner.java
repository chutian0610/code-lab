package info.victorchu.snippets.clazz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描工具
 * @author victorchu
 * @date 2022/8/5 21:35
 */
public class ClassScanner {

    /**
     * scan class under package name
     * @param packageName package Name to scan
     * @param recursion recursive search
     */
    public static List<String> scanForPackage(String packageName, boolean recursion) throws IOException {
        List<String> classNames = new ArrayList<>();
        // get current class loader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // convert package name to dir path
        String packagePath = packageName.replace(".", "/");

        List<URL> urls = Collections.list(loader.getResources(packagePath));
        for (URL url:urls) {
            if(url!=null){
                String protocol = url.getProtocol();
                if("file".equals(protocol)){
                    classNames.addAll(scanClassInDir(url.getPath(),recursion));
                }else {
                    if("jar".equals(protocol)){
                        classNames.addAll(scanClassInJar(url.getPath(),recursion));
                    }
                    else {
                        throw new RuntimeException("unknown protocol of class source");
                    }
                }
            }else {
                // if you disable "Add directory entries" when creating jar, it maybe null
                classNames.addAll(scanClassInJar(url.getPath(),recursion));
            }
        }

        return classNames;
    }

    private static List<String> scanClassInDir(String path,boolean recursion){
        List<String> classNames = new ArrayList<>();
        File file = new File(path);
        File[] childFiles = file.listFiles();
        for (File child: childFiles){
            if (child.isDirectory()){
                if(recursion){
                    classNames.addAll(scanClassInDir(child.getPath(),recursion));
                }

            }else {
                String childPath = child.getPath();
                if(childPath.endsWith(".class")){
                    childPath = childPath.substring(childPath.lastIndexOf(File.separator+"classes")+9,childPath.lastIndexOf("."));
                    childPath =childPath.replace(File.separator,".");
                    classNames.add(childPath);
                }
            }
        }
        return classNames;
    }

    private static List<String> scanClassInJar(String path,boolean recursion){
        List<String> classNames = new ArrayList<>();
        int index = path.lastIndexOf("!");
        String pathInfo = path.substring(0,index);
        pathInfo = pathInfo.substring(pathInfo.indexOf(File.separator));
        String packgeInfo = path.substring(index+2);
        JarFile jar = null;
        try {
            jar = new JarFile(pathInfo);
            List<JarEntry> list = Collections.list(jar.entries());
            for (JarEntry jarEntry:list){
                String name = jarEntry.getName();
                if(name.endsWith(".class")){
                    if(recursion){
                        if(name.startsWith(packgeInfo)){
                            name = name.substring(0,name.lastIndexOf(".")).replace(File.separator,".");
                            classNames.add(name);
                        }
                    }else {
                        int pos = name.lastIndexOf(File.separator);
                        String myPackagePath;
                        if (pos != -1) {
                            myPackagePath = name.substring(0, pos);
                        } else {
                            myPackagePath = name;
                        }
                        if (myPackagePath.equals(packgeInfo)) {
                            name = name.substring(0, name.lastIndexOf(".")).replace(File.separator, ".");
                            classNames.add(name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(jar !=null){
                try {
                    jar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return classNames;
    }
}
