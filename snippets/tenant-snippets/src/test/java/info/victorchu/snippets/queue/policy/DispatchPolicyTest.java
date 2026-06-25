package info.victorchu.snippets.queue.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import info.victorchu.snippets.queue.api.DispatchPlan;
import info.victorchu.snippets.queue.api.DynamicQueue;
import info.victorchu.snippets.queue.api.QueueConfig;
import info.victorchu.snippets.queue.api.Task;
import info.victorchu.snippets.queue.core.DefaultDynamicQueue;

/**
 * 跨队列调度策略测试。
 */
class DispatchPolicyTest {

    private static DynamicQueue queue(String name, int waiting) {
        DefaultDynamicQueue q = new DefaultDynamicQueue(QueueConfig.of(name, 100, 5, new FairScheduler()));
        for (int i = 0; i < waiting; i++) {
            q.tryEnqueue(noopTask(name + "-" + i));
        }
        return q;
    }

    private static Task noopTask(String id) {
        return new Task() {
            @Override public String id() { return id; }
            @Override public String targetQueue() { return id.split("-")[0]; }
            @Override public int priority() { return 1; }
            @Override public long submittedAt() { return 0; }
            @Override public void execute() { }
        };
    }

    /** 校验 permits 总和 == totalPermit,且每个 permit >= 0。 */
    private static void assertPermitSum(DispatchPlan p, int totalPermit) {
        int sum = 0;
        for (Map.Entry<String, Integer> e : p.permits().entrySet()) {
            assertTrue(e.getValue() >= 0, "permit 应非负:" + e);
            sum += e.getValue();
        }
        assertEquals(totalPermit, sum, "permits 总和必须等于 totalPermit, plan=" + p);
    }

    // ----------------- FifoDispatchPolicy -----------------

    @Test
    void fifoEvenSplitWithRemainder() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0), queue("c", 0));
        DispatchPlan p = new FifoDispatchPolicy().plan(qs, 7);
        // 7/3 = 2 余 1，前 1 个队列多 1
        assertEquals(Arrays.<String>asList("a", "b", "c"), p.orderedNames());
        assertEquals(3, (int) p.permits().get("a"));
        assertEquals(2, (int) p.permits().get("b"));
        assertEquals(2, (int) p.permits().get("c"));
        assertPermitSum(p, 7);
    }

    @Test
    void fifoEmptyInput() {
        DispatchPlan p = new FifoDispatchPolicy().plan(Collections.<DynamicQueue>emptyList(), 10);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void fifoZeroPermit() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 5), queue("b", 3));
        DispatchPlan p = new FifoDispatchPolicy().plan(qs, 0);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void fifoSingleQueue() {
        Collection<DynamicQueue> qs = Collections.singletonList(queue("solo", 0));
        DispatchPlan p = new FifoDispatchPolicy().plan(qs, 9);
        assertEquals(Collections.singletonList("solo"), p.orderedNames());
        assertEquals(9, (int) p.permits().get("solo"));
        assertPermitSum(p, 9);
    }

    @Test
    void fifoPermitLessThanQueueCount() {
        // permit < n 时每队列 0 或 1
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0), queue("c", 0), queue("d", 0));
        DispatchPlan p = new FifoDispatchPolicy().plan(qs, 2);
        // permits.size() == 队列数(4),每条 permit 为 0 或 1,sum=2
        assertEquals(4, p.permits().size());
        int total = 0;
        for (int v : p.permits().values()) {
            assertTrue(v == 0 || v == 1, "permit 应为 0 或 1, 实际=" + v);
            total += v;
        }
        assertEquals(2, total);
        assertPermitSum(p, 2);
    }

    @Test
    void fifoPreservesInputOrder() {
        // 队列入参顺序就是出参顺序（与积压无关）
        Collection<DynamicQueue> qs = Arrays.asList(queue("z", 100), queue("a", 0), queue("m", 50));
        DispatchPlan p = new FifoDispatchPolicy().plan(qs, 6);
        assertEquals(Arrays.asList("z", "a", "m"), p.orderedNames());
    }

    // ----------------- RoundRobinDispatchPolicy -----------------

    @Test
    void roundRobinPermitsRotate() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0), queue("c", 0));
        // 用同一实例让游标持续推进
        RoundRobinDispatchPolicy rr = new RoundRobinDispatchPolicy();
        DispatchPlan p1 = rr.plan(qs, 5);
        assertEquals(2, (int) p1.permits().get("a"));
        assertEquals(2, (int) p1.permits().get("b"));
        assertEquals(1, (int) p1.permits().get("c"));
        assertPermitSum(p1, 5);
        // 第二次游标 +1，从 b 起
        DispatchPlan p2 = rr.plan(qs, 5);
        assertEquals(Arrays.asList("b", "c", "a"), p2.orderedNames());
    }

    @Test
    void roundRobinEmptyInput() {
        DispatchPlan p = new RoundRobinDispatchPolicy().plan(Collections.<DynamicQueue>emptyList(), 10);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void roundRobinZeroPermit() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 5), queue("b", 3));
        DispatchPlan p = new RoundRobinDispatchPolicy().plan(qs, 0);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void roundRobinSingleQueue() {
        Collection<DynamicQueue> qs = Collections.singletonList(queue("solo", 0));
        RoundRobinDispatchPolicy rr = new RoundRobinDispatchPolicy();
        DispatchPlan p = rr.plan(qs, 7);
        assertEquals(Collections.singletonList("solo"), p.orderedNames());
        assertEquals(7, (int) p.permits().get("solo"));
        assertPermitSum(p, 7);
    }

    @Test
    void roundRobinPermitEqualsQueueCount() {
        // permit == n 时,每队列恰 1 个
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0), queue("c", 0));
        RoundRobinDispatchPolicy rr = new RoundRobinDispatchPolicy();
        DispatchPlan p = rr.plan(qs, 3);
        assertEquals(1, (int) p.permits().get("a"));
        assertEquals(1, (int) p.permits().get("b"));
        assertEquals(1, (int) p.permits().get("c"));
        assertPermitSum(p, 3);
    }

    @Test
    void roundRobinCursorCycles() {
        // 游标推进 3 次后应回到起点
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0), queue("c", 0));
        RoundRobinDispatchPolicy rr = new RoundRobinDispatchPolicy();
        List<String> p1 = rr.plan(qs, 3).orderedNames();
        List<String> p2 = rr.plan(qs, 3).orderedNames();
        List<String> p3 = rr.plan(qs, 3).orderedNames();
        List<String> p4 = rr.plan(qs, 3).orderedNames();
        assertEquals(p1, p4);
        // 每轮起点比上轮后移 1
        assertEquals("b", p2.get(0));
        assertEquals("c", p3.get(0));
        assertEquals("a", p4.get(0));
    }

    // ----------------- BacklogDispatchPolicy -----------------

    @Test
    void backlogProportionalToWaitingSize() {
        Collection<DynamicQueue> qs = Arrays.asList(
                queue("a", 10), queue("b", 20), queue("c", 30));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 12);
        // 总积压 60，比例 a:2/12, b:4/12, c:6/12
        // 实际：a=2, b=4, c=6（最后一个拿余数）
        Map<String, Integer> permits = p.permits();
        assertEquals(2, (int) permits.get("a"));
        assertEquals(4, (int) permits.get("b"));
        assertEquals(6, (int) permits.get("c"));
        assertPermitSum(p, 12);
    }

    @Test
    void backlogAllEmptyFallsBackToFifo() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 0), queue("b", 0));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 4);
        // 退化为 FIFO 均分
        assertEquals(2, (int) p.permits().get("a"));
        assertEquals(2, (int) p.permits().get("b"));
        assertPermitSum(p, 4);
    }

    @Test
    void backlogEmptyInput() {
        DispatchPlan p = new BacklogDispatchPolicy().plan(Collections.<DynamicQueue>emptyList(), 10);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void backlogZeroPermit() {
        Collection<DynamicQueue> qs = Arrays.asList(queue("a", 5), queue("b", 3));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 0);
        assertTrue(p.orderedNames().isEmpty());
        assertTrue(p.permits().isEmpty());
    }

    @Test
    void backlogSingleQueue() {
        Collection<DynamicQueue> qs = Collections.singletonList(queue("solo", 42));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 7);
        assertEquals(Collections.singletonList("solo"), p.orderedNames());
        assertEquals(7, (int) p.permits().get("solo"));
        assertPermitSum(p, 7);
    }

    @Test
    void backlogOrderingByWaitingDescending() {
        // 验证 orderedNames 按 waitingSize 降序
        Collection<DynamicQueue> qs = Arrays.asList(
                queue("small", 1), queue("huge", 100), queue("mid", 10));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 9);
        assertEquals(Arrays.asList("huge", "mid", "small"), p.orderedNames());
        assertPermitSum(p, 9);
    }

    @Test
    void backlogExtremeSkew() {
        // 一边倒积压,验证仍按比例分 permit
        Collection<DynamicQueue> qs = Arrays.asList(
                queue("big", 1000), queue("tiny", 1));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 100);
        // 比例 1000/1001 ≈ 99.9,1/1001 ≈ 0.1
        // 公式:big=1000*100/1001=99, tiny=max(1, 1*100/1001)=max(1,0)=1
        // 但 big 不是最后一个,tiny 是,tiny 拿余数 100-99=1
        assertEquals(99, (int) p.permits().get("big"));
        assertEquals(1, (int) p.permits().get("tiny"));
        assertPermitSum(p, 100);
    }

    @Test
    void backlogEachQueueGetsAtLeastOneWhenBacklogExists() {
        // 任何队列只要 waiting > 0,就至少分 1 个 permit（即使比例极小）
        Collection<DynamicQueue> qs = Arrays.asList(
                queue("a", 1), queue("b", 1), queue("c", 1));
        DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 3);
        // 比例均为 1/3, 取整=1, 最后一个拿余数 3-2=1
        assertEquals(1, (int) p.permits().get("a"));
        assertEquals(1, (int) p.permits().get("b"));
        assertEquals(1, (int) p.permits().get("c"));
        assertPermitSum(p, 3);
    }

    @Test
    void backlogPermitSumAlwaysMatches() {
        // 多组随机分布,反复验证 permits 总和 == totalPermit
        int[][] distributions = {
                {3, 7, 5, 1},
                {100, 0, 50, 25, 75},  // 含 0 会走 FIFO 退化分支
                {10, 10, 10},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        };
        String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        for (int[] dist : distributions) {
            java.util.List<DynamicQueue> qs = new java.util.ArrayList<>();
            for (int i = 0; i < dist.length; i++) {
                qs.add(queue(names[i], dist[i]));
            }
            DispatchPlan p = new BacklogDispatchPolicy().plan(qs, 17);
            assertPermitSum(p, 17);
        }
    }
}
