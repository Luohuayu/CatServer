package com.conversantmedia.util.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Created by jcairns on 2/12/16.
 */
public interface OptimisticLock {

    /**
     * Aquire a lock token for reading
     *
     * @return long - the token indicating the lock state
     */
    long readLock();

    /**
     * check if optimistic locking succeeded
     *
     * @param lockToken - the value returned from tryLock
     * @return boolean - true if lock was held
     */
    boolean readLockHeld(long lockToken);

    /**
     *  Acquire the lock for writing, waiting if needed
     *
     * @return long - the token indicating the lock state
     */
    long writeLock();

    /**
     * @return long - the token indicating the lock state
     *
     * @throws InterruptedException - on interrupt
     */
    long tryWriteLockInterruptibly() throws InterruptedException;

    /**
     * @return long - the token indicating the lock state, or 0 if not available
     */
    long tryWriteLock();

    /**
     * @return long - the token indicating the lock state, or 0 if not available
     *
     * @throws InterruptedException on interrupt
     */
    long tryWriteLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * "commit" or unlock the sequence when the write lock is held
     *
     * @param sequence - lock sequence to unlock
     */
    void unlock(final long sequence);

}
