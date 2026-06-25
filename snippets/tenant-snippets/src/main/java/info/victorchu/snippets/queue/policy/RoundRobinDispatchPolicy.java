package info.victorchu.snippets.queue.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import info.victorchu.snippets.queue.api.DispatchPlan;
import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueDispatchPolicy;

/**
 * 轮询（Round Robin）跨队列调度。
 * <p>维护一个内部游标，每次 plan 时游标推进 1。本轮访问顺序从游标开始。</p>
 *
 * <p><b>permit 分配</b>：先每队列取 1（min(n, P) 个），剩余 permit 再轮转分配，直到用完。
 * 这样 permit &gt; 队列数时仍能保持轮转特性。</p>
 *
 * <p><b>复杂度</b>：O(n + P)（n 队列数，P 本轮总 permit）。</p>
 */
public class RoundRobinDispatchPolicy implements QueueDispatchPolicy {

    private final AtomicInteger cursor = new AtomicInteger(0);

    @Override
    public DispatchPlan plan(Collection<? extends DynamicQueue> queues, int totalPermit) {
        if (queues.isEmpty() || totalPermit <= 0) {
            return DispatchPlan.of(Collections.<String>emptyList(), Collections.<String, Integer>emptyMap());
        }

        List<DynamicQueue> list = new ArrayList<>(queues);
        int n = list.size();
        // 防负取模（cursor 增长可能溢出 int，但 Math.floorMod 内部已处理）
        int start = Math.floorMod(cursor.getAndIncrement(), n);

        // 1) 构造访问顺序（从 start 起）
        List<String> ordered = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            ordered.add(list.get((start + i) % n).name());
        }

        // 2) 轮转分配 permit：按轮次 n 个一组循环,直到 permit 用完。
        // 边界:n=1 时退化为"全给唯一队列",所以外层不能限制为 round < n。
        Map<String, Integer> permits = new LinkedHashMap<>(n);
        int remaining = totalPermit;
        while (remaining > 0) {
            for (int i = 0; i < n && remaining > 0; i++) {
                permits.merge(ordered.get(i), 1, Integer::sum);
                remaining--;
            }
        }
        return DispatchPlan.of(ordered, permits);
    }
}
