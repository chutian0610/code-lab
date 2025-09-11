package info.victorchu.snippets.concurrency.threadpool.shared;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MultiQueueDualSemaphore<E extends ResourceManagedTask> extends AbstractQueue<E> implements BlockingQueue<E> {
    private static class TaskScheduler {
        final Semaphore enqueueSemaphore; // 入队信号量
        final Semaphore runSemaphore; // 运行信号量
        private final ReentrantLock lock;

        final AtomicInteger runningCount = new AtomicInteger(0);
        final AtomicInteger enqueuedCount = new AtomicInteger(0);

        TaskScheduler(int enqueueLimit, int runLimit,ReentrantLock lock) {
            this.lock = lock;
            this.enqueueSemaphore = new Semaphore(enqueueLimit);
            this.runSemaphore = new Semaphore(runLimit);
        }
    }

    private ResourceGroupManager resourceGroupManager;
    private ConcurrentLinkedQueue<ResourceGroup> resourceGroupList = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<String,TaskScheduler> taskSchedulerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PriorityBlockingQueue<E>> queueMap = new ConcurrentHashMap<>();
    // 锁和条件变量
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final AtomicInteger currentSize = new AtomicInteger(0);
    // 队列容量控制
    private final int capacity;

    public MultiQueueDualSemaphore(int capacity,ResourceGroupManager resourceGroupManager) {
        this.resourceGroupManager = resourceGroupManager;
        this.capacity = capacity;
        resourceGroupManager.getResourceGroups().forEach(resourceGroup -> {
            resourceGroupList.add(resourceGroup);
            taskSchedulerMap.put(resourceGroup.getName().toUpperCase(), new TaskScheduler(resourceGroup.getQueueSize(), resourceGroup.getRunningSize(),lock));
            queueMap.put(resourceGroup.getName().toUpperCase(), new PriorityBlockingQueue<>());
        });
    }
    public MultiQueueDualSemaphore(ResourceGroupManager resourceGroupManager) {
        this(Integer.MAX_VALUE,resourceGroupManager);
    }

    @Override
    public boolean offer(E task, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ResourceGroup resourceGroup = resourceGroupManager.getResourceGroup(task);
        if (resourceGroup == null) {
            throw new IllegalArgumentException("未知任务类型: " + task.getResourceGroupId());
        }
        TaskScheduler scheduler = taskSchedulerMap.get(resourceGroup.getName().toUpperCase());
        if (scheduler == null) {
            throw new IllegalArgumentException("未知任务类型: " + resourceGroup.getName());
        }

        // 获取入队信号量
        if (!scheduler.enqueueSemaphore.tryAcquire()) {
            if (nanos <= 0) return false;
            // 考虑到下游压力，这里快速失败
            return false;
        }
        lock.lockInterruptibly();
        try {
            // 2. 检查队列容量
            while (currentSize.get() >= capacity) {
                if (nanos <= 0) {
                    scheduler.enqueueSemaphore.release(); // 超时释放信号量
                    return false;
                }
                nanos = notFull.awaitNanos(nanos);
            }
            // 入队到对应类型的队列
            PriorityBlockingQueue<E> typeQueue = queueMap.get(resourceGroup.getName().toUpperCase());
            boolean added = typeQueue.offer(task);

            if (added) {
                currentSize.incrementAndGet();
                scheduler.enqueuedCount.incrementAndGet();
                // 唤醒等待的消费线程
                notEmpty.signal();
            } else {
                // 添加失败，释放入队信号量
                scheduler.enqueueSemaphore.release();
            }
            return added;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean offer(E task) {
        try {
            return offer(task, 0, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (true) {
                E task = findRunnableTask();
                if (task != null) {
                    return (E)task;
                }
                notEmpty.await();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E poll() {
        lock.lock();
        try {
            E task = findRunnableTask();
            if (task != null) {
                return (E)task;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly();
        try {
            while (true) {
                E task = findRunnableTask();
                if (task != null) {
                    return task;
                }
                if (nanos <= 0) {
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E peek() {
        // 返回优先级最高的任务（不移除）
        lock.lock();
        try {
            List<ResourceGroup> resourceGroups = new ArrayList<>(resourceGroupList);
            for (ResourceGroup resourceGroup : resourceGroups) {
                PriorityBlockingQueue<E> typeQueue = queueMap.get(resourceGroup.getName().toUpperCase());
                if (!typeQueue.isEmpty()) {
                    return typeQueue.peek();
                }
            }
            return null;
        }finally{
            lock.unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        // 返回所有队列的合并迭代器
        List<Iterator<E>> iterators = new ArrayList<>();
        queueMap.values().forEach(queue -> iterators.add(queue.iterator()));
        return new MultiIterator<>(iterators);
    }

    @Override
    public int size() {
        return currentSize.get();
    }
    @Override
    public int remainingCapacity() {
        return capacity - currentSize.get();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        int count = 0;
        lock.lock();
        try {
            while (count < maxElements) {
                E task = findRunnableTask();
                if (task == null) break;
                c.add(task);
                count++;
            }
            return count;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(E task) throws InterruptedException {
        offer(task, Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    /**
     * 任务执行完成回调
     */
    public void taskCompleted(String taskType) {
        TaskScheduler semaphores = taskSchedulerMap.get(taskType.toUpperCase());
        if (semaphores != null) {
            semaphores.runSemaphore.release();
            semaphores.runningCount.decrementAndGet();
            lock.lock();
            try {
                notEmpty.signal(); // 唤醒可能等待的出队操作
            }finally {
                lock.unlock();
            }
        }
    }

    /**
     * 查找可以运行的任务
     */
    private E findRunnableTask() {
        List<ResourceGroup> resourceGroups = new ArrayList<>(resourceGroupList);
        for (ResourceGroup resourceGroup : resourceGroups) {
            PriorityBlockingQueue<E> typeQueue = queueMap.get(resourceGroup.getName().toUpperCase());
            if (typeQueue.isEmpty()) {
                continue;
            }
            TaskScheduler semaphores = taskSchedulerMap.get(resourceGroup.getName().toUpperCase());
            if (semaphores.runSemaphore.tryAcquire()) {
                E task = typeQueue.poll();
                if (task != null) {
                    currentSize.decrementAndGet();
                    notFull.signal();
                    // 释放入队信号量（任务开始执行）
                    semaphores.enqueueSemaphore.release();
                    semaphores.enqueuedCount.decrementAndGet();
                    semaphores.runningCount.incrementAndGet();
                    return task;
                } else {
                    // 队列为空，释放运行信号量
                    semaphores.runSemaphore.release();
                }

            }
        }
        return null;
    }

    // 多迭代器实现
    private static class MultiIterator<E> implements Iterator<E> {
        private final List<Iterator<E>> iterators;
        private int currentIndex = 0;

        MultiIterator(List<Iterator<E>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            while (currentIndex < iterators.size()) {
                if (iterators.get(currentIndex).hasNext()) {
                    return true;
                }
                currentIndex++;
            }
            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterators.get(currentIndex).next();
        }
    }

    /**
     * 清空队列
     */
    public void clear() {
        lock.lock();
        try {
            queueMap.values().forEach(PriorityBlockingQueue::clear);
            currentSize.set(0);
            // 释放所有入队信号量
            taskSchedulerMap.values().forEach(semaphores -> {
                int permits = semaphores.enqueueSemaphore.drainPermits();
                semaphores.enqueueSemaphore.release(permits);
            });
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
