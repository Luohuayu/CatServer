package com.conversantmedia.util.concurrent;

/*
 * #%L
 * Conversant Disruptor
 * ~~
 * Conversantmedia.com © 2016, Conversant, Inc. Conversant® is a trademark of Conversant, Inc.
 * ~~
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Alternative implementation for benchmarking purposes
 *
 * @author John Cairns {@literal <john@2ad.com>} Date: 4/20/16
 */
public final class MPMCBlockingQueue<E> extends MPMCConcurrentQueue<E> implements Serializable, Iterable<E>, Collection<E>, BlockingQueue<E>, Queue<E>, ConcurrentQueue<E> {

    // locking objects used for independent locking
    // of not empty, not full status, for java BlockingQueue support
    // if MultithreadConcurrentQueue is used directly, these calls are
    // optimized out and have no impact on timing values
    //
    protected final Condition queueNotFullCondition;
    protected final Condition queueNotEmptyCondition;

    /**
     * <p>
     * Construct a blocking queue of the given fixed capacity.
     * </p>
     * Note: actual capacity will be the next power of two
     * larger than capacity.
     *
     * @param capacity maximum capacity of this queue
     */

    public MPMCBlockingQueue(final int capacity) {
        // waiting locking gives substantial performance improvements
        // but makes disruptor aggressive with cpu utilization
        this(capacity, SpinPolicy.WAITING);
    }

    /**
     * <p>
     * Construct a blocking queue with a given fixed capacity
     * </p>
     * Note: actual capacity will be the next power of two
     * larger than capacity.
     *
     * Waiting locking may be used in servers that are tuned for it, waiting
     * locking provides a high performance locking implementation which is approximately
     * a factor of 2 improvement in throughput (40M/s for 1-1 thread transfers)
     *
     * However waiting locking is more CPU aggressive and causes servers that may be
     * configured with far too many threads to show very high load averages.   This is probably
     * not as detrimental as it is annoying.
     *
     * @param capacity - the queue capacity, suggest using a power of 2
     * @param spinPolicy - determine the level of cpu aggressiveness in waiting
     */
    public MPMCBlockingQueue(final int capacity, final SpinPolicy spinPolicy) {
        super(capacity);

        switch(spinPolicy) {
            case BLOCKING:
                queueNotFullCondition = new QueueNotFull();
                queueNotEmptyCondition = new QueueNotEmpty();
                break;
            case SPINNING:
                queueNotFullCondition = new SpinningQueueNotFull();
                queueNotEmptyCondition = new SpinningQueueNotEmpty();
                break;
            case WAITING:
            default:
                queueNotFullCondition = new WaitingQueueNotFull();
                queueNotEmptyCondition = new WaitingQueueNotEmpty();
        }
    }

    /**
     * <p>
     * Construct a blocking queue of the given fixed capacity
     * </p><p>
     * Note: actual capacity will be the next power of two
     * larger than capacity.
     * </p>
     * The values from the collection, c, are appended to the
     * queue in iteration order.     If the number of elements
     * in the collection exceeds the actual capacity, then the
     * additional elements overwrite the previous ones until
     * all elements have been written once.
     *
     * @param capacity maximum capacity of this queue
     * @param c        A collection to use to populate inital values
     */
    public MPMCBlockingQueue(final int capacity, Collection<? extends E> c) {
        this(capacity);
        for (final E e : c) {
            offer(e);
        }
    }

    @Override
    public final boolean offer(E e) {
        if (super.offer(e)) {
            queueNotEmptyCondition.signal();
            return true;
        } else {
            queueNotEmptyCondition.signal();
            return false;
        }
    }

    @Override
    public final E poll() {
        final E e = super.poll();
        // not full now
        queueNotFullCondition.signal();
        return e;
    }

    @Override
    public int remove(final E[] e) {
        final int n = super.remove(e);
        // queue can not be full
        queueNotFullCondition.signal();
        return n;
    }

    @Override
    public E remove() {
        return poll();
    }

    @Override
    public E element() {
        final E val = peek();
        if (val != null)
            return val;
        throw new NoSuchElementException("No element found.");
    }

    @Override
    public void put(E e) throws InterruptedException {
        // add object, wait for space to become available
        while (offer(e) == false) {
            if(Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            queueNotFullCondition.await();
        }
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        for (;;) {
            if (offer(e)) {
                return true;
            } else {

                // wait for available capacity and try again
                if (!Condition.waitStatus(timeout, unit, queueNotFullCondition)) return false;
            }
        }
    }

    @Override
    public E take() throws InterruptedException {
        for (;;) {
            E pollObj = poll();
            if (pollObj != null) {
                return pollObj;
            }
            if(Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            queueNotEmptyCondition.await();
        }
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        for(;;) {
            E pollObj = poll();
            if(pollObj != null) {
                return pollObj;
            } else {
                // wait for the queue to have at least one element or time out
                if(!Condition.waitStatus(timeout, unit, queueNotEmptyCondition)) return null;
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        queueNotFullCondition.signal();
    }

    @Override
    public int remainingCapacity() {
        return size - size();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, size());
    }

    @Override
    // drain the whole queue at once
    public int drainTo(Collection<? super E> c, int maxElements) {

        // required by spec
        if (this == c) throw new IllegalArgumentException("Can not drain to self.");

        // batch remove is not supported in MPMC
        int nRead = 0;

        while(!isEmpty() && maxElements > 0) {
            final E e = poll();
            if(e != null) {
                c.add(e);
                nRead++;
            }
        }

        // only return the number that was actually added to the collection
        return nRead;
    }


    @Override
    public Object[] toArray() {
        final E[] e = (E[]) new Object[size()];

        toArray(e);

        return e;

    }

    @Override
    public <T> T[] toArray(T[] a) {

        remove((E[]) a);

        return a;
    }

    @Override
    public boolean add(E e) {
        if (offer(e)) return true;
        throw new IllegalStateException("queue is full");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (final Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (final E e : c) {
            if (!offer(e)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new RingIter();
    }

    private final boolean isFull() {
        final long queueStart = tail.get() - size;
        return head.get() == queueStart;
    }

    private final class RingIter implements Iterator<E> {
        int dx = 0;

        E lastObj = null;

        private RingIter() {

        }

        @Override
        public boolean hasNext() {
            return dx < size();
        }

        @Override
        public E next() {
            final long pollPos = head.get();
            final int slot = (int) ((pollPos + dx++) & mask);
            lastObj = buffer[slot].entry;
            return lastObj;
        }

        @Override
        public void remove() {
            MPMCBlockingQueue.this.remove(lastObj);
        }
    }

    // condition used for signaling queue is full
    private final class QueueNotFull extends AbstractCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class QueueNotEmpty extends AbstractCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

    // condition used for signaling queue is full
    private final class WaitingQueueNotFull extends AbstractWaitingCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class WaitingQueueNotEmpty extends AbstractWaitingCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

    private final class SpinningQueueNotFull extends AbstractSpinningCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class SpinningQueueNotEmpty extends AbstractSpinningCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

}
