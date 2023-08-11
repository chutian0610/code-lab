package info.victorchu.snippets.performance.circuitbreaker;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * @author victorchu
 * @date 2022/10/11 20:36
 */
@Slf4j
public class MemoryCircuitBreaker implements CircuitBreaker{
    private static final MemoryMXBean MEMORY_MX_BEAN = ManagementFactory.getMemoryMXBean();

    private final Long currentMaxHeap = MEMORY_MX_BEAN.getHeapMemoryUsage().getMax(); ;
    private final ThreadLocal<Long> seenMemory = ThreadLocal.withInitial(() -> 0L);
    private final ThreadLocal<Long> allowedMemory = ThreadLocal.withInitial(() -> 0L);

    private CircuitBreakerConfig circuitBreakerConfig;

    public MemoryCircuitBreaker(CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    @Override
    public boolean isEnabled() {
        return circuitBreakerConfig.isMemCBEnabled();
    }

    @Override
    public boolean isTripped() {
        if(!isEnabled()){
            return false;
        }
        long localAllowedMemory = getCurrentMemoryThreshold();
        long localSeenMemory = calculateLiveMemoryUsage();
        allowedMemory.set(localAllowedMemory);

        seenMemory.set(localSeenMemory);

        return (localSeenMemory >= localAllowedMemory);
    }
    private long getCurrentMemoryThreshold() {
        int thresholdValueInPercentage = circuitBreakerConfig.getMemCBThreshold();
        double thresholdInFraction = thresholdValueInPercentage / (double) 100;
        return (long) (currentMaxHeap * thresholdInFraction);
    }

    protected long calculateLiveMemoryUsage() {
        // NOTE: MemoryUsageGaugeSet provides memory usage statistics but we do not use them
        // here since it will require extra allocations and incur cost, hence it is cheaper to use
        // MemoryMXBean directly. Ideally, this call should not add noticeable
        // latency to a query -- but if it does, please signify on SOLR-14588
        return MEMORY_MX_BEAN.getHeapMemoryUsage().getUsed();
    }

    @Override
    public String getDebugInfo() {
        if (seenMemory.get() == 0L || allowedMemory.get() == 0L) {
            log.warn("MemoryCircuitBreaker's monitored values (seenMemory, allowedMemory) not set");
        }

        return "seenMemory=" + seenMemory.get() + " allowedMemory=" + allowedMemory.get();
    }

    @Override
    public String getErrorMessage() {
        return "Memory Circuit Breaker triggered as JVM heap usage values are greater than allocated threshold."
                + "Seen JVM heap memory usage "
                + seenMemory.get()
                + " and allocated threshold "
                + allowedMemory.get();
    }
}
