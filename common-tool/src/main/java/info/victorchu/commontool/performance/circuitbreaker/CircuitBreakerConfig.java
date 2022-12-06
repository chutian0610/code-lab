package info.victorchu.commontool.performance.circuitbreaker;

import lombok.Data;

/**
 * @author victorchu
 * @date 2022/10/11 20:37
 */
@Data
public class CircuitBreakerConfig {
    private boolean enabled;
    /**
     * 是否开启内存熔断器
     */
    private boolean memCBEnabled;
    private int memCBThreshold;
}