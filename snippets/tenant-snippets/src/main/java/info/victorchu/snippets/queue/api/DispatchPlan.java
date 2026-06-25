package info.victorchu.snippets.queue.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 跨队列调度计划。
 *
 * <p>由 {@link QueueDispatchPolicy#plan} 返回，Manager 据此遍历队列并按额度拉取。</p>
 *
 * <p>手写实现以避免 lombok 注解处理器在 JDK 25 上的兼容性问题。</p>
 */
public final class DispatchPlan {

    private final List<String> orderedNames;
    private final Map<String, Integer> permits;

    public DispatchPlan(List<String> orderedNames, Map<String, Integer> permits) {
        this.orderedNames = Collections.unmodifiableList(Objects.requireNonNull(orderedNames, "orderedNames"));
        this.permits = Collections.unmodifiableMap(Objects.requireNonNull(permits, "permits"));
    }

    public static DispatchPlan of(List<String> orderedNames, Map<String, Integer> permits) {
        return new DispatchPlan(orderedNames, permits);
    }

    public List<String> orderedNames() {
        return orderedNames;
    }

    public Map<String, Integer> permits() {
        return permits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DispatchPlan)) {
            return false;
        }
        DispatchPlan that = (DispatchPlan) o;
        return orderedNames.equals(that.orderedNames) && permits.equals(that.permits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderedNames, permits);
    }

    @Override
    public String toString() {
        return "DispatchPlan{orderedNames=" + orderedNames + ", permits=" + permits + "}";
    }
}
