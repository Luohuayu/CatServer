package com.conversantmedia.util.collection;

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
 * Created by jcairns on 6/11/14.
 */
public interface Stack<N> {

    /**
     * Linear search the stack for contains - not an efficient operation
     *
     * @param n - Object to test for containment
     * @return boolean - true if n is contained somewhere in the stack
     */
    boolean contains(N n);

    /**
     * Add the element to the stack top, optionally failing if there is
     * no capacity (overflow)
     *
     * @param n - element to push
     * @return boolean - true if push succeeded
     */
    boolean push(N n);

    /**
     * show the current stack top
     * @return N - the element at the top of the stack or null
     */
    N peek();

    /**
     * pop and return the element from the top of the stack
     *
     * @return N - the element, or null if the stack is empty
     */
    N pop();

    /**
     * @return int - the size of the stack in number of elements
     */
    int size();

    /**
     * @return int - the number of empty slots available in the stack
     */
    int remainingCapacity();

    /**
     * @return boolean - true if stack is empty
     */
    boolean isEmpty();

    /**
     * clear the stack
     */
    void clear();

}
