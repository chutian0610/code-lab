/**
 * 正则表达式引擎toy。
 * <pre>
 *  支持语法:
 *      1. s|t 或
 *      2. st 连接
 *      3. s*重复
 *      4. 单个字符
 *  功能:
 *      1. ε-NFA 构造
 *      2. NFA -> DFA
 *      3. DFA 化简
 *      4. 引擎执行(NFA and DFA)
 * </pre>
 * @date 2022/2/11 5:08 下午
 * @author victorchutian
 */
package info.victorchu.toy.compiler.simpleregex;