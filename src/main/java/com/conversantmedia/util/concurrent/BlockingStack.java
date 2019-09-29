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

import com.conversantmedia.util.collection.Stack;

import java.util.concurrent.TimeUnit;

/**
 * Created by jcairns on 2/16/16.
 */
public interface BlockingStack<N> extends Stack<N> {

    /**
     * Push an element on the stack, waiting if necessary if the stack is currently full
     *
     * @param n - the element to push on the stack
     * @param time - the maximum time to wait
     * @param unit - unit of waiting time
     * @return boolean - true if item was pushed, false otherwise
     *
     * @throws InterruptedException on interrupt
     */
    boolean push(final N n, final long time, final TimeUnit unit) throws InterruptedException;

    /**
     * Push an element on the stack waiting as long as required for space to become available
     *
     * @param n - the element to push
     * @throws InterruptedException - in the event the current thread is interrupted prior to pushing the element
     */
    void pushInterruptibly(final N n) throws InterruptedException;

    /**
     * Pop an element from the stack, waiting if necessary if the stack is currently empty
     *
     * @param time - the maximum time to wait
     * @param unit - the time unit for the waiting time
     * @return N - the popped element, or null in the event of a timeout
     *
     * @throws InterruptedException on interrupt
     */
    N pop(final long time, final TimeUnit unit) throws InterruptedException;

    /**
     * Pop an element from the stack, waiting as long as required for an element to become available on the
     * stack
     *
     * @return N - the popped element
     * @throws InterruptedException - in the event the current thread is interrupted prior to popping any element
     */
    N popInterruptibly() throws InterruptedException;

}
