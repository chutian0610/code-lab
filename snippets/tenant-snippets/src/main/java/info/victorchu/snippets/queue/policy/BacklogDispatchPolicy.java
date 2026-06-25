package info.victorchu.snippets.queue.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import info.victorchu.snippets.queue.api.DispatchPlan;
import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueDispatchPolicy;

/**
 * 积压优先跨队列调度。
 * <p>按队列 {@link DynamicQueue#waitingSize()} 降序分配 permit。
 * 积压量大的队列本轮拿更多，反之更少。</p>
 *
 * <p><b>分配公式</b>：{@code permit_i = max(1, waitingSize_i * totalPermit / totalBacklog)}，
 * 最后一个队列拿剩余 permit 避免舍入误差。</p>
 *
 * <p><b>退化</b>：所有队列 waitingSize 为 0 时退化为 FIFO 均分。</p>
 *
 * <p><b>线程安全</b>：无状态，可共享单例。</p>
 */
public class BacklogDispatchPolicy implements QueueDispatchPolicy {

    /** 退化分支复用：所有队列空时退化为 FIFO，避免每轮 new 一个对象 */
    private static final FifoDispatchPolicy FIFO_FALLBACK = new FifoDispatchPolicy();

    /**
     * 内部快照：把 {@code name} 与 {@code waitingSize} 一次性收集到本地结构，
     * 后续排序/求和/分配全部使用快照值，避免反复调用 {@link DynamicQueue#waitingSize()}（每次都会加锁）。
     */
    private static final class Snapshot {
        final String name;
        final int size;

        Snapshot(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }

    @Override
    public DispatchPlan plan(Collection<? extends DynamicQueue> queues, int totalPermit) {
        if (queues.isEmpty() || totalPermit <= 0) {
            return DispatchPlan.of(Collections.<String>emptyList(), Collections.<String, Integer>emptyMap());
        }

        // 1) 一次性快照：把 name + waitingSize 收集到本地（仅此阶段触碰队列锁）
        List<Snapshot> snapshot = new ArrayList<>(queues.size());
        for (DynamicQueue q : queues) {
            snapshot.add(new Snapshot(q.name(), q.waitingSize()));
        }

        // 2) 按 size 降序排序（使用快照值，零额外加锁）
        snapshot.sort((a, b) -> Integer.compare(b.size, a.size));

        // 3) 用快照值累加 totalBacklog
        long totalBacklog = 0L;
        for (Snapshot s : snapshot) {
            totalBacklog += s.size;
        }
        // 所有队列空：退化为 FIFO
        if (totalBacklog == 0L) {
            return FIFO_FALLBACK.plan(queues, totalPermit);
        }

        // 4) 按快照值分配 permit；最后一个队列拿剩余 permit 避免舍入误差
        List<String> ordered = new ArrayList<>(snapshot.size());
        Map<String, Integer> permits = new LinkedHashMap<>(snapshot.size());
        int assigned = 0;
        for (int i = 0; i < snapshot.size(); i++) {
            Snapshot s = snapshot.get(i);
            ordered.add(s.name);
            int p;
            if (i == snapshot.size() - 1) {
                // 最后一个队列拿剩余 permit，确保 sum == totalPermit
                p = totalPermit - assigned;
            } else {
                p = (int) Math.max(1L, (long) s.size * totalPermit / totalBacklog);
            }
            permits.put(s.name, p);
            assigned += p;
        }
        return DispatchPlan.of(ordered, permits);
    }
}
