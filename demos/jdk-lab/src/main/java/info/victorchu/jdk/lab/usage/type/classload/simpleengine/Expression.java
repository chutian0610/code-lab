package info.victorchu.jdk.lab.usage.type.classload.simpleengine;

import java.util.Map;

/**
 * 表达式接口
 */
public interface Expression {
    Object run(Map<String,Object> env);
}
