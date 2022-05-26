/**
 * <pre>
 * 构造如下的class包装expression
 * <code>
 * public class ${script_name} {
 *     public Object run0(${args}){
 *         ${expression}
 *     }
 *     public Object run(Map&lt;String,Object> env){
 *         return run0(env.get(key1),...)
 *     }
 * }
 * </code>
 *
 * 1. 改造javaCompiler api,实现内存编译
 * 2. 通过Class<?>.newInstance() 获得Expression实例
 * 3. 执行expression
 *
 * use demo:
 * <code>
 * public static void main(String[] args){
 *   SimpleExpressionEngine simpleExpressionEngine = new SimpleExpressionEngine(null);
 *   String  exp = "return 1000+100.0*99-(600-3*15)/(((68-9)-3)*2-100)+10000%7*71;";
 *   Map&lt;String,Object> env = new HashMap<>();
 *   System.out.println(simpleExpressionEngine.evalute(exp,env));
 * }
 * </code>
 * </pre>
 */
package info.victorchu.type.classload.simpleengine;