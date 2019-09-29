package com.conversantmedia.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * http://www.hpl.hp.com/techreports/2012/HPL-2012-68.pdf
 *
 * Created by jcairns on 2/12/16.
 */
public class SequenceLock implements OptimisticLock {
    final ContendedAtomicLong sequence = new ContendedAtomicLong(2);

    @Override
    public long readLock() {
        // anybody can read until the sequence changes
        return sequence.get();
    }

    @Override
    public boolean readLockHeld(final long lockToken) {
        return sequence.get() == lockToken &&
                (lockToken & 1) == 0;
    }

    @Override
    public long writeLock() {
        int spin = 0;
        for(;;) {
            final long sequence = this.sequence.get();
            if(((sequence & 1) == 0) &&
                    this.sequence.compareAndSet(sequence, sequence+1)) {
                return sequence;
            }
            spin = Condition.progressiveYield(spin);
        }
    }

    @Override
    public void unlock(final long sequence) {
        this.sequence.set(sequence+2L);
    }

    @Override
    public long tryWriteLock() {
        final long sequence = this.sequence.get();
        if(((sequence & 1) == 0) &&
                this.sequence.compareAndSet(sequence, sequence+1)) {
            return sequence;
        }
        return 0;
    }

    @Override
    public long tryWriteLock(final long time, final TimeUnit unit) throws InterruptedException {
        final long toNanos = System.nanoTime() + unit.toNanos(time);

        long sequence = tryWriteLock();

        int spin = 0;
        while(sequence == 0 && toNanos - System.nanoTime() > 0) {
            if(Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            Condition.progressiveYield(spin);
            sequence = tryWriteLock();
        }

        return sequence;
    }


    @Override
    public long tryWriteLockInterruptibly() throws InterruptedException {

        long sequence = tryWriteLock();

        int spin = 0;
        while(sequence == 0) {
            if(Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
            Condition.progressiveYield(spin);
            sequence = tryWriteLock();
        }

        return sequence;
    }

}
