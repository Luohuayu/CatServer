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
import java.util.concurrent.locks.LockSupport;

/*
 * Return true once a condition is satisfied
 * Created by jcairns on 12/11/14.
 */
interface Condition {

    long PARK_TIMEOUT = 50L;

    int MAX_PROG_YIELD = 2000;

    // return true if the queue condition is satisfied
    boolean test();

    // wake me when the condition is satisfied, or timeout
    void awaitNanos(final long timeout) throws InterruptedException;

    // wake if signal is called, or wait indefinitely
    void await() throws InterruptedException;

    // tell threads waiting on condition to wake up
    void signal();

    /*
     * progressively transition from spin to yield over time
     */
    static int progressiveYield(final int n) {
        if(n > 500) {
            if(n<1000) {
                // "randomly" yield 1:8
                if((n & 0x7) == 0) {
                    LockSupport.parkNanos(PARK_TIMEOUT);
                } else {
                    onSpinWait();
                }
            } else if(n<MAX_PROG_YIELD) {
                // "randomly" yield 1:4
                if((n & 0x3) == 0) {
                    Thread.yield();
                } else {
                    onSpinWait();
                }
            } else {
                Thread.yield();
                return n;
            }
        } else {
            onSpinWait();
        }
        return n+1;
    }

    static void onSpinWait() {

        // Java 9 hint for spin waiting PAUSE instruction

        //http://openjdk.java.net/jeps/285
        // Thread.onSpinWait();
    }

    /**
     * Wait for timeout on condition
     *
     * @param timeout - the amount of time in units to wait
     * @param unit - the time unit
     * @param condition - condition to wait for
     * @return boolean - true if status was detected
     * @throws InterruptedException - on interrupt
     */
    static boolean waitStatus(final long timeout, final TimeUnit unit, final Condition condition) throws InterruptedException {
        // until condition is signaled

        final long timeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
        final long expireTime = System.nanoTime() + timeoutNanos;
        // the queue is empty or full wait for something to change
        while (condition.test()) {
            final long now = System.nanoTime();
            if (now > expireTime) {
                return false;
            }

            condition.awaitNanos(expireTime - now - PARK_TIMEOUT);

        }

        return true;
    }

}
