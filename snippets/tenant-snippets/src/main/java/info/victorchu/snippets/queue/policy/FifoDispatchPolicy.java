package info.victorchu.snippets.queue.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.victorchu.snippets.queue.api.DispatchPlan;
import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueDispatchPolicy;

/**
 * FIFO 跨队列调度（默认）。
 * <p>按队列在 Manager 中的插入顺序遍历；本轮总 permit 均分到每个队列，余数加给前几个。</p>
 *
 * <p><b>复杂度</b>：O(n)（n 为队列数）。</p>
 *
 * <p><b>线程安全</b>：无状态，可共享单例。</p>
 */
public class FifoDispatchPolicy implements QueueDispatchPolicy {

    @Override
    public DispatchPlan plan(Collection<? extends DynamicQueue> queues, int totalPermit) {
        if (queues.isEmpty() || totalPermit <= 0) {
            return DispatchPlan.of(Collections.<String>emptyList(), Collections.<String, Integer>emptyMap());
        }

        List<DynamicQueue> list = new ArrayList<>(queues);
        int n = list.size();
        int base = totalPermit / n;
        int rem = totalPermit % n;

        List<String> ordered = new ArrayList<>(n);
        // permits 用 HashMap：访问顺序由 orderedNames 决定，permits 自身无需保留插入顺序
        Map<String, Integer> permits = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            DynamicQueue q = list.get(i);
            ordered.add(q.name());
            // 前 rem 个队列多拿 1 个，余数补偿
            permits.put(q.name(), base + (i < rem ? 1 : 0));
        }
        return DispatchPlan.of(ordered, permits);
    }
}
