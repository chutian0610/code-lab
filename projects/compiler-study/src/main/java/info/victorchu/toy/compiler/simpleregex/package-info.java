/**
 * 正则表达式引擎toy。
 * <pre>
 *  支持语法:
 *      1. s|t 或
 *      2. st 连接
 *      3. s*重复
 *      4. 单个字符
 *      5. 括号
 *  功能:
 *      1. ε-NFA 构造
 *      2. NFA -> DFA
 *      3. DFA 化简
 *      4. 引擎执行(NFA and DFA)
 * </pre>
 * @author victorchutian
 */
package info.victorchu.toy.compiler.simpleregex;