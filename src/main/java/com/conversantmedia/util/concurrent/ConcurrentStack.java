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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Concurrent "lock-free" version of a stack.
 *
 * @author John Cairns
 * <p>Date: 7/9/12</p>
 */
public final class ConcurrentStack<N> implements BlockingStack<N> {

    private final int size;

    private final AtomicReferenceArray<N> stack;

    // representing the top of the stack
    private final ContendedAtomicInteger stackTop = new ContendedAtomicInteger(0);

    private final SequenceLock  seqLock = new SequenceLock();

    private final Condition stackNotFullCondition;
    private final Condition stackNotEmptyCondition;

    public ConcurrentStack(final int size) {
        this(size, SpinPolicy.WAITING);
    }

    /**
     *   construct a new stack of given capacity
     *
     *  @param size - the stack size
     *  @param spinPolicy - determine the level of cpu aggressiveness in waiting
     */
    public ConcurrentStack(final int size, final SpinPolicy spinPolicy) {
        int stackSize = 1;
        while(stackSize < size) stackSize <<=1;
        this.size = stackSize;
        stack = new AtomicReferenceArray<N>(stackSize);

        switch(spinPolicy) {
            case BLOCKING:
                stackNotFullCondition = new StackNotFull();
                stackNotEmptyCondition = new StackNotEmpty();
                break;
            case SPINNING:
                stackNotFullCondition = new SpinningStackNotFull();
                stackNotEmptyCondition = new SpinningStackNotEmpty();
                break;
            case WAITING:
            default:
                stackNotFullCondition = new WaitingStackNotFull();
                stackNotEmptyCondition = new WaitingStackNotEmpty();
        }
    }


    @Override
    public final boolean push(final N n, final long time, final TimeUnit unit) throws InterruptedException {
        final long endDate = System.nanoTime() + unit.toNanos(time);
        while(!push(n)) {
            if(endDate - System.nanoTime() < 0) {
                return false;
            }

            Condition.waitStatus(time, unit, stackNotFullCondition);
        }
        stackNotEmptyCondition.signal();
        return true;
    }

    @Override
    public final void pushInterruptibly(final N n) throws InterruptedException {
        while(!push(n)) {
            if(Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            stackNotFullCondition.await();
        }
        stackNotEmptyCondition.signal();
    }

    @Override
    public final boolean contains(final N n) {
        if(n != null) {
            for(int i = 0; i<stackTop.get(); i++) {
                if(n.equals(stack.get(i))) return true;
            }
        }
        return false;
    }

    /**
     * add an element to the stack, failing if the stack is unable to grow
     *
     * @param n - the element to push
     *
     * @return boolean - false if stack overflow, true otherwise
     */
    @Override
    public final boolean push(final N n) {
        int spin = 0;
        for(;;) {

            final long writeLock = seqLock.tryWriteLock();
            if(writeLock>0L) {
                try {
                    final int stackTop = this.stackTop.get();
                    if(size>stackTop) {
                        try {
                            stack.set(stackTop, n);
                            stackNotEmptyCondition.signal();
                            return true;
                        } finally {
                            this.stackTop.set(stackTop+1);
                        }
                    } else {
                        return false;
                    }
                } finally {
                    seqLock.unlock(writeLock);
                }

            }
	        spin = Condition.progressiveYield(spin);
        }
    }

    /**
     *  peek at the top of the stack
     *
     * @return N - the object at the top of the stack
     */
    @Override
    public final N peek() {
        // read the current cursor
        int spin = 0;
        for(;;) {
            final long readLock = seqLock.readLock();
            final int stackTop = this.stackTop.get();
            if(stackTop > 0) {
                final N  n = stack.get(stackTop-1);
                if(seqLock.readLockHeld(readLock)) {
                    return n;
                } // else loop again
            } else {
                return null;
            }

            spin = Condition.progressiveYield(spin);
        }
    }

    /**
     * pop the next element off the stack
     * @return N - The object on the top of the stack
     */
    @Override
    public final N pop() {

        int spin = 0;
        // now pop the stack
        for(;;) {
            final long writeLock = seqLock.tryWriteLock();
            if(writeLock > 0) {
                try {
                    final int stackTop = this.stackTop.get();
                    final int lastRef = stackTop-1;
                    if(stackTop>0) {
                        try {
                            // if we can modify the stack - i.e. nobody else is modifying
                            final N n = stack.get(lastRef);
                            stack.set(lastRef, null);
                            stackNotFullCondition.signal();
                            return n;
                        } finally {
                            this.stackTop.set(lastRef);
                        }
                    } else {
                        return null;
                    }
                } finally {
                    seqLock.unlock(writeLock);
                }
            }

            spin = Condition.progressiveYield(spin);

        }
    }

    @Override
    public final N pop(final long time, final TimeUnit unit) throws InterruptedException {
        final long endTime = System.nanoTime() + unit.toNanos(time);
        for(;;) {
            final N n = pop();
            if(n != null) {
                stackNotFullCondition.signal();
                return n;
            } else {
                if(endTime - System.nanoTime() < 0) {
                    return null;
                }
            }
            Condition.waitStatus(time, unit, stackNotEmptyCondition);
        }
    }

    @Override
    public final N popInterruptibly() throws InterruptedException {
        for(;;) {
            final N n = pop();
            if(n != null) {
                stackNotFullCondition.signal();
                return n;
            } else {
                if(Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
            }
            stackNotEmptyCondition.await();
        }
    }

    /**
     * Return the size of the stack
     * @return int - number of elements in the stack
     */
    @Override
    public final int size() {
        return stackTop.get();
    }

    /**
     * how much available space in the stack
     */
    @Override
    public final int remainingCapacity() {
        return size - stackTop.get();
    }

    /**
     * @return boolean - true if stack is currently empty
     */
    @Override
    public final boolean isEmpty() {
        return stackTop.get()==0;
    }

    /**
     *  clear the stack - does not null old references
     */
    @Override
    public final void clear() {
        int spin = 0;
        for(;;) {
            final long writeLock = seqLock.tryWriteLock();
            if(writeLock > 0L) {
                final int stackTop = this.stackTop.get();
                if(stackTop>0) {
                    try {
                        for(int i = 0; i<stackTop; i++) {
                            stack.set(i, null);
                        }
                        stackNotFullCondition.signal();
                        return;
                    } finally {
                        this.stackTop.set(0);
                    }
                } else {
                    return;
                }
            }

            spin = Condition.progressiveYield(spin);
        }
    }

    private boolean isFull() {
        return size == stackTop.get();
    }


    // condition used for signaling queue is full
    private final class WaitingStackNotFull extends AbstractWaitingCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class WaitingStackNotEmpty extends AbstractWaitingCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

    // condition used for signaling queue is full
    private final class SpinningStackNotFull extends AbstractSpinningCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class SpinningStackNotEmpty extends AbstractSpinningCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

    // condition used for signaling queue is full
    private final class StackNotFull extends AbstractCondition {

        @Override
        // @return boolean - true if the queue is full
        public final boolean test() {
            return isFull();
        }
    }

    // condition used for signaling queue is empty
    private final class StackNotEmpty extends AbstractCondition {
        @Override
        // @return boolean - true if the queue is empty
        public final boolean test() {
            return isEmpty();
        }
    }

}
