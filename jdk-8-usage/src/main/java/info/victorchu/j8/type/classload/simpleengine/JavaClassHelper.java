package info.victorchu.j8.type.classload.simpleengine;


import javax.tools.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JavaClassHelper {

    private Map<String,Object> classLoaders = new HashMap<>();
    private Map<String,Script> scriptMap = new HashMap<>();

    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            classLoaders.put(name,classLoader);
            return classLoader.loadClass(name);
        }
    }

    public Script loadScript(Script script,String scriptPrefixName,Map<String,Object> env) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, IOException {
        if(scriptMap.containsKey(script.getOriginExp())){
            return scriptMap.get(script.getOriginExp());
        }else {
            // 生成java code
            script = ScriptFactory.generateScriptFromExpression(scriptPrefixName,script,env);
            // 生成 java class
            Map<String, byte[]> map = generateJavaClassFromCode(script);
            // 加载class
            Class<?> scriptClass = loadClass(script.getClassName(),map);
            // 构造实例
            script.setExpression((Expression) scriptClass.newInstance());
            // cache
            scriptMap.put(script.getOriginExp(),script);
            return script;
        }
    }


    private Map<String, byte[]> generateJavaClassFromCode(Script script) throws IOException {
        // 获取java 编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 声明监听器
        DiagnosticCollector<JavaFileObject>  diagnostics = new DiagnosticCollector<>();
        // 文件管理器
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(diagnostics, Locale.CHINA, Charset.forName("UTF-8"));
        // 装饰类
        MemoryJavaFileManager memManager = new MemoryJavaFileManager(stdManager);

        // 所要编译的源文件
        JavaFileObject javaFileObject = memManager.makeStringSource(script.getClassName()+".java", script.getJavaClass());
        JavaCompiler.CompilationTask task = compiler.getTask(null, memManager,
                diagnostics, null, null, Arrays.asList(javaFileObject));
        // 如果没有编译警告和错误,这个call() 方法会编译 javaFileObject 以及有依赖关系的可编译的文件.
        Boolean result = task.call();
        /* 只有当所有的编译单元都执行成功了,这个 call() 方法才返回 Boolean.TRUE  . 一旦有任何错误,这个方法就会返回 Boolean.FALSE .
         */
        for(Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.printf(
                    "Code: %s%n" +
                            "Kind: %s%n" +
                            "Position: %s%n" +
                            "Start Position: %s%n" +
                            "End Position: %s%n" +
                            "Source: %s%n" +
                            "Message:  %s%n",
                    diagnostic.getCode(), diagnostic.getKind(),
                    diagnostic.getPosition(), diagnostic.getStartPosition(),
                    diagnostic.getEndPosition(), diagnostic.getSource(),
                    diagnostic.getMessage(null));
        }
        if (result == null || !result.booleanValue()) {
            throw new RuntimeException("Compilation failed.");
        }
        return memManager.getClassBytes();
    }
}