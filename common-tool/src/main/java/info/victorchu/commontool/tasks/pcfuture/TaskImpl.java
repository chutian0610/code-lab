package info.victorchu.commontool.tasks.pcfuture;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * Task 的 默认实现.
 * @see java.util.concurrent.FutureTask
 * @author victorchu
 * @date 2022/8/8 14:10
 */
@Slf4j
public class TaskImpl implements Task{
    public static final AtomicLong ID = new AtomicLong(0);
    private final Long id;

    public TaskImpl(){
        id = ID.incrementAndGet();
        state = NEW;
    }

    /**
     * task的状态
     */
    private volatile int state;
    private volatile Future<?> future;

    private static final int NEW = 0;
    private static final int PUT = 1;
    private static final int TAKE = 2;
    private static final int COMPLETING = 3;
    private static final int COMPLETE = 4;
    private static final int EXCEPTIONAL  = 5;
    private static final int CANCELLED = 6;
    private static final int INTERRUPTING = 7;
    private static final int INTERRUPTED  = 8;

    private volatile Object outcome = null;

    private volatile WaitNode waiters;

    @Override
    public boolean isCancelled() {
        return state >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state >= COMPLETE;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // state == new means not submit queue
        if (!(state == NEW &&
                UNSAFE.compareAndSwapInt(this, stateOffset, NEW,
                        mayInterruptIfRunning ? INTERRUPTING : CANCELLED))) {
            return false;
        }

        if (!(state == PUT &&
                UNSAFE.compareAndSwapInt(this, stateOffset, PUT,
                        mayInterruptIfRunning ? INTERRUPTING : CANCELLED))) {
            return false;
        }

        if(state >=TAKE){
            UNSAFE.putOrderedInt(this, stateOffset, mayInterruptIfRunning ? INTERRUPTING : CANCELLED);
            try {
                future.cancel(mayInterruptIfRunning);
            }catch (Exception e){

            }
            finally {
                UNSAFE.putOrderedInt(this, stateOffset, INTERRUPTED);
            }
        }
        return true;
    }

    @SneakyThrows(SubmitException.class)
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        int s = state;
        if( s == NEW){
            throw new SubmitException("Task not submitted");
        }
        if (s <= COMPLETING) {
            s = awaitDone(false, 0L);
        }
        return report(s);
    }

    @SneakyThrows(SubmitException.class)
    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = state;
        if( s == NEW){
            throw new SubmitException("Task not submitted");
        }
        if (s <= COMPLETING &&
                (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        return report(s);
    }

    private Object report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == COMPLETE) {
            return outcome;
        }
        if (s >= CANCELLED) {
            throw new CancellationException();
        }
        throw new ExecutionException((Throwable)x);
    }

    @Override
    public void setResult(Object v) {
        while (state<COMPLETING &&UNSAFE.compareAndSwapInt(this, stateOffset, TAKE, COMPLETING)) {
            outcome = v;
            UNSAFE.putOrderedInt(this, stateOffset, COMPLETE);
            finishCompletion();
        }

    }

    @Override
    public void setException(Throwable t) {
        while (state<COMPLETING && UNSAFE.compareAndSwapInt(this, stateOffset, TAKE, COMPLETING)) {
            outcome = t;
            UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL);
            finishCompletion();
        }
    }


    @Override
    public void onPut() {
        if (state<PUT && UNSAFE.compareAndSwapInt(this, stateOffset, NEW, PUT)) {

        }
    }

    @Override
    public void onTake(Future<?> future) {
        if (state<TAKE && UNSAFE.compareAndSwapInt(this, stateOffset, PUT, TAKE)) {
            this.future = future;
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName()+"-"+id;
    }

    private void finishCompletion() {
        for (WaitNode q; (q = waiters) != null;) {
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, q, null)) {
                for (;;) {
                    Thread t = q.thread;
                    if (t != null) {
                        q.thread = null;
                        LockSupport.unpark(t);
                    }
                    WaitNode next = q.next;
                    if (next == null) {
                        break;
                    }
                    q.next = null; // unlink to help gc
                    q = next;
                }
                break;
            }
        }
    }

    private int awaitDone(boolean timed, long nanos)
            throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode q = null;
        boolean queued = false;
        if(log.isDebugEnabled()){
            log.debug("awaitDone:{}",getName());
        }
        for (;;) {
            if (Thread.interrupted()) {
                removeWaiter(q);
                throw new InterruptedException();
            }

            int s = state;
            if (s > COMPLETING) {
                if (q != null) {
                    q.thread = null;
                }
                return s;
            }
            else if (s == COMPLETING) // cannot time out yet
            {
                Thread.yield();
            } else if (q == null) {
                q = new WaitNode();
            } else if (!queued) {
                queued = UNSAFE.compareAndSwapObject(this, waitersOffset,
                        q.next = waiters, q);
            } else if (timed) {
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L) {
                    removeWaiter(q);
                    return state;
                }
                LockSupport.parkNanos(this, nanos);
            }
            else {
                LockSupport.park(this);
            }
        }
    }

    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (;;) {          // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null) {
                        pred = q;
                    } else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) // check for race
                        {
                            continue retry;
                        }
                    }
                    else if (!UNSAFE.compareAndSwapObject(this, waitersOffset,
                            q, s)) {
                        continue retry;
                    }
                }
                break;
            }
        }
    }


    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;
        WaitNode() { thread = Thread.currentThread(); }
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long waitersOffset;
    private static final long stateOffset;
    static {
        try {
            UNSAFE =createUnsafe();
            Class<?> k = TaskImpl.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
            waitersOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("waiters"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static Unsafe createUnsafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

