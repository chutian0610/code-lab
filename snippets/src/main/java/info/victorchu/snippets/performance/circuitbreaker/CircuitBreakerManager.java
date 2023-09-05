package info.victorchu.snippets.performance.circuitbreaker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victorchu

 */
public class CircuitBreakerManager {
    private CircuitBreakerConfig circuitBreakerConfig;
    private List<CircuitBreaker> circuitBreakerList;

    public CircuitBreakerManager(CircuitBreakerConfig circuitBreakerConfig, List<CircuitBreaker> circuitBreakerList) {
        this.circuitBreakerConfig = circuitBreakerConfig;
        this.circuitBreakerList = circuitBreakerList;
    }

    public boolean isEnabled(){
        return circuitBreakerConfig.isEnabled();
    }

    public List<CircuitBreaker> checkTripped() {
        List<CircuitBreaker> triggeredCircuitBreakers = null;
        if (isEnabled()) {
            for (CircuitBreaker circuitBreaker : circuitBreakerList) {
                if (circuitBreaker.isEnabled() && circuitBreaker.isTripped()) {
                    if (triggeredCircuitBreakers == null) {
                        triggeredCircuitBreakers = new ArrayList<>();
                    }

                    triggeredCircuitBreakers.add(circuitBreaker);
                }
            }
        }

        return triggeredCircuitBreakers;
    }
    public boolean checkAnyTripped() {
        if (isEnabled()) {
            for (CircuitBreaker circuitBreaker : circuitBreakerList) {
                if (circuitBreaker.isEnabled() && circuitBreaker.isTripped()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String toErrorMessage(List<CircuitBreaker> circuitBreakerList) {
        StringBuilder sb = new StringBuilder();

        for (CircuitBreaker circuitBreaker : circuitBreakerList) {
            sb.append(circuitBreaker.getErrorMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
