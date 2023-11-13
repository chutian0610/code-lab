package info.victorchu.demos.javainst;

import java.lang.instrument.Instrumentation;

/**
 * @author victorchu
 */
public class InstrumentationHelper
{
    public static volatile Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation inst)
    {
        System.out.println("[Agent] premain method set Instrumentation");
        InstrumentationHelper.inst = inst;
        transformClass("info.victorchu.demos.javainst.Application", inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst)
    {
        System.out.println("[Agent] In agentmain method");
        InstrumentationHelper.inst = inst;
        transformClass("info.victorchu.demos.javainst.Application", inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation)
    {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation);
            return;
        }
        catch (Exception ex) {
            System.out.println("Class not found with Class.forName");
        }
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation)
    {
        MethodCallTracer dt = new MethodCallTracer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        }
        catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }
}
