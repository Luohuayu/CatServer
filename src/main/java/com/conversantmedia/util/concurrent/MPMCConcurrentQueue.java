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


/**
 * Dmitry Vyukov, Bounded MPMC queue - http://www.1024cores.net/home/lock-free-algorithms/queues/bounded-mpmc-queue
 *
 * Added for benchmarking and comparison.     MultithreadConcurrentQueue performs better in the regimes I have tested.
 *
 * Created by jcairns on 5/29/14.
 */
class MPMCConcurrentQueue<E> implements ConcurrentQueue<E> {

    protected final int      size;

    final long     mask;

    // a ring buffer representing the queue
    final Cell<E>[] buffer;

    final ContendedAtomicLong head = new ContendedAtomicLong(0L);

    final ContendedAtomicLong tail = new ContendedAtomicLong(0L);

    /**
     * Construct a blocking queue of the given fixed capacity.
     *
     * Note: actual capacity will be the next power of two
     * larger than capacity.
     *
     * @param capacity maximum capacity of this queue
     */

    public MPMCConcurrentQueue(final int capacity) {
        // capacity of at least 2 is assumed
        int c = 2;
        while(c < capacity) c <<=1;
        size = c;
        mask = size - 1L;
        buffer = new Cell[size];
        for(int i=0; i<size; i++) {
            buffer[i] = new Cell<E>(i);
        }
    }

    @Override
    public boolean offer(E e) {
        Cell<E> cell;
        long tail = this.tail.get();
        for(;;) {
            cell = buffer[(int)(tail & mask)];
            final long seq = cell.seq.get();
            final long dif = seq - tail;
            if(dif == 0) {
                if(this.tail.compareAndSet(tail, tail+1)) {
                    break;
                }
            } else if(dif < 0) {
                return false;
            } else {
                tail = this.tail.get();
            }
        }
        cell.entry = e;
        cell.seq.set(tail + 1);
        return true;
    };

    @Override
    public E poll() {
        Cell<E> cell;
        long head = this.head.get();
        for(;;) {
            cell = buffer[(int)(head & mask)];
            long seq = cell.seq.get();
            final long dif = seq - (head+1L);
            if(dif == 0) {
                if(this.head.compareAndSet(head, head+1)) {
                    break;
                }
            } else if(dif < 0) {
                return null;
            } else {
                head = this.head.get();
            }
        }

        try {
            return cell.entry;
        } finally {
            cell.entry = null;
            cell.seq.set(head + mask + 1L);
        }

    }

    @Override
    public final E peek() {
        return buffer[(int)(head.get()&mask)].entry;
    }


    @Override
    // drain the whole queue at once
    public int remove(final E[] e) {
        int nRead = 0;
        while(nRead < e.length && !isEmpty()) {
            final E entry = poll();
            if(entry != null) {
                e[nRead++] = entry;
            }
        }
        return nRead;
    }

    @Override
    public final int size() {
        return (int)Math.max((tail.get() - head.get()), 0);
    }

    @Override
    public int capacity() {
        return size;
    }

    @Override
    public final boolean isEmpty() {
        return head.get() == tail.get();
    }

    @Override
    public void clear() {
        while(!isEmpty()) poll();
    }

    @Override
    public final boolean contains(Object o) {
        for(int i=0; i<size(); i++) {
            final int slot = (int)((head.get() + i) & mask);
            if(buffer[slot].entry != null && buffer[slot].entry.equals(o)) return true;
        }
        return false;

    }

    protected static final class Cell<R> {
        final ContendedAtomicLong seq = new ContendedAtomicLong(0L);

        public long p1, p2, p3, p4, p5, p6, p7;

        R entry;

        public long a1, a2, a3, a4, a5, a6, a7, a8;

        Cell(final long s) {
            seq.set(s);
            entry = null;
        }

        public long sumToAvoidOptimization() {
            return p1+p2+p3+p4+p5+p6+p7+a1+a2+a3+a4+a5+a6+a7+a8;
        }

    }

}
