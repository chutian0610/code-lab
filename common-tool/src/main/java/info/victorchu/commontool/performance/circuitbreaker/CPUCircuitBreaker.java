package info.victorchu.commontool.performance.circuitbreaker;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * @Date:2023/2/7 11:18
 * @Author:victorchutian
 */
@Slf4j
public class CPUCircuitBreaker implements CircuitBreaker{
    private static final OperatingSystemMXBean operatingSystemMXBean =
            ManagementFactory.getOperatingSystemMXBean();

    // Assumption -- the value of these parameters will be set correctly before invoking
    // getDebugInfo()
    private static final ThreadLocal<Double> seenCPUUsage = ThreadLocal.withInitial(() -> 0.0);

    private static final ThreadLocal<Double> allowedCPUUsage = ThreadLocal.withInitial(() -> 0.0);

    private CircuitBreakerConfig circuitBreakerConfig;

    public CPUCircuitBreaker(CircuitBreakerConfig config) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    @Override
    public boolean isEnabled() {
        return circuitBreakerConfig.isCpuCBEnabled();
    }

    @Override
    public boolean isTripped() {
        if (!isEnabled()) {
            return false;
        }

        double localAllowedCPUUsage = getCpuUsageThreshold();
        double localSeenCPUUsage = calculateLiveCPUUsage();

        if (localSeenCPUUsage < 0) {
            if (log.isWarnEnabled()) {
                String msg = "Unable to get CPU usage";

                log.warn(msg);
            }

            return false;
        }

        allowedCPUUsage.set(localAllowedCPUUsage);

        seenCPUUsage.set(localSeenCPUUsage);

        return (localSeenCPUUsage >= localAllowedCPUUsage);
    }

    @Override
    public String getDebugInfo() {
        if (seenCPUUsage.get() == 0.0 || seenCPUUsage.get() == 0.0) {
            log.warn("CPUCircuitBreaker's monitored values (seenCPUUSage, allowedCPUUsage) not set");
        }

        return "seenCPUUSage=" + seenCPUUsage.get() + " allowedCPUUsage=" + allowedCPUUsage.get();
    }

    @Override
    public String getErrorMessage() {
        return "CPU Circuit Breaker triggered as seen CPU usage is above allowed threshold."
                + "Seen CPU usage "
                + seenCPUUsage.get()
                + " and allocated threshold "
                + allowedCPUUsage.get();
    }

    public double getCpuUsageThreshold() {
        return circuitBreakerConfig.getCpuCBThreshold();
    }

    protected double calculateLiveCPUUsage() {
        return operatingSystemMXBean.getSystemLoadAverage();
    }
}
