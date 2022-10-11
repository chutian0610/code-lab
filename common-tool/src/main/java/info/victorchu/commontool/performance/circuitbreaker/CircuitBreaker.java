package info.victorchu.commontool.performance.circuitbreaker;

/**
 * @author victorchu
 * @date 2022/10/11 20:23
 */
public interface CircuitBreaker {
    /**
     * 是否开启断路器
     * @return
     */
    boolean isEnabled();

    /**
     * 检查是否触发
     */
    boolean isTripped();

    /**
     * 获取用于debug的信息
     * @return
     */
    String getDebugInfo();

    /**
     * 获取错误信息
     * @return
     */
    String getErrorMessage();
}
