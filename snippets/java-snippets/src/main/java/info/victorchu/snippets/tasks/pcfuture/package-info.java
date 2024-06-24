/**
 * producer & consumer Future
 * <pre>
 *  1. 直接使用线程池做任务控制并不是那么友好，于是将队列抽取出来
 *  2. 实现了基于队列的同步|异步 task 处理
 *  task 链路:
 *  producer -> task limit blocking queue -> consumer -> task executor
 *           阻塞                                     阻塞
 * </pre>

 *

 * @author victorchu
 */
package info.victorchu.snippets.tasks.pcfuture;