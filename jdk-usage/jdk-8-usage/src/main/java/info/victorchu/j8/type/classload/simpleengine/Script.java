package info.victorchu.j8.type.classload.simpleengine;

import java.lang.reflect.Method;

/**
 * 简单脚本
 */
public class Script {
    // 原始表达式
    private String originExp;
    // java class name
    private String className;
    // 生成的java代码
    private String javaClass;
    private Class<?> runClass;
    private Method method;
    private Expression expression;

    public Script() {
    }

    public String getOriginExp() {
        return originExp;
    }

    public void setOriginExp(String originExp) {
        this.originExp = originExp;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
