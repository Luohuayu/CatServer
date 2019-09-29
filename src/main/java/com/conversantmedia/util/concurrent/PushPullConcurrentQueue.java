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

import java.util.concurrent.atomic.LongAdder;

/**
 * Tuned version of Martin Thompson's push pull queue
 *
 * Transfers from a single thread writer to a single thread reader are orders of nanoseconds (3-5)
 *
 * This code is optimized and tested using a 64bit HotSpot JVM on an Intel x86-64 environment.  Other
 * environments should be carefully tested before using in production.
 *
 * Created by jcairns on 5/28/14.
 */
public class PushPullConcurrentQueue<E> implements ConcurrentQueue<E> {

    final int size;

    final long mask;

    final LongAdder tail = new LongAdder();

    long p1, p2, p3, p4, p5, p6, p7;
    long tailCache = 0L;
    long a1, a2, a3, a4, a5, a6, a7, a8;

    final E[] buffer;

    long r1, r2, r3, r4, r5, r6, r7;
    long headCache = 0L;
    long c1, c2, c3, c4, c5, c6, c7, c8;

    final LongAdder head = new LongAdder();

    public PushPullConcurrentQueue(final int capacity) {
        this.size = Capacity.getCapacity(capacity);
        this.mask = this.size-1;

        buffer = (E[])new Object[this.size];
    }


    @Override
    public boolean offer(final E e) {
        if(e != null) {
            final long tail = this.tail.sum();
            final long queueStart = tail - size;
            if((headCache > queueStart) || ((headCache = head.sum()) > queueStart)) {
                final int dx = (int) (tail & mask);
                buffer[dx] = e;
                this.tail.increment();
                return true;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException("Invalid element");
        }
    }

    @Override
    public E poll() {
        final long head = this.head.sum();
        if((head < tailCache) || (head < (tailCache = tail.sum()))) {
            final int dx = (int)(head & mask);
            final E e = buffer[dx];
            buffer[dx] = null;

            this.head.increment();
            return e;
        } else {
            return null;
        }
    }

    @Override
    public int remove(final E[] e) {
        int n = 0;

        headCache = this.head.sum();

        final int nMax = e.length;
        for(long i = headCache, end = tail.sum(); n<nMax && i<end; i++) {
            final int dx = (int) (i & mask);
            e[n++] = buffer[dx];
            buffer[dx] = null;
        }

        this.head.add(n);

        return n;
    }

    @Override
    public void clear() {
        for(int i=0; i<buffer.length; i++) {
            buffer[i] = null;
        }
        head.add(tail.sum()-head.sum());
    }


    @Override
    public final E peek() {
        return buffer[(int)(head.sum() & mask)];
    }

    /**
     * This implemention is known to be broken if preemption were to occur after
     * reading the tail pointer.
     *
     * Code should not depend on size for a correct result.
     *
     * @return int - possibly the size, or possibly any value less than capacity()
     */
    @Override
    public final int size() {
        return (int)Math.max(tail.sum() - head.sum(), 0);
    }

    @Override
    public int capacity() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return tail.sum() == head.sum();
    }

    @Override
    public final boolean contains(Object o) {
        if(o != null) {
            for(long i = head.sum(), end = tail.sum(); i<end; i++) {
                final E e = buffer[(int)(i & mask)];
                if(o.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    long sumToAvoidOptimization() {
        return p1+p2+p3+p4+p5+p6+p7+a1+a2+a3+a4+a5+a6+a7+a8+r1+r2+r3+r4+r5+r6+r7+c1+c2+c3+c4+c5+c6+c7+c8+headCache+tailCache;
    }
}
