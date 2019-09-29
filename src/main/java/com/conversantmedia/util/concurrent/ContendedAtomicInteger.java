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

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Avoid false cache line sharing
 *
 * Created by jcairns on 5/28/14.
 */
final class ContendedAtomicInteger {

    private static final int CACHE_LINE_INTS = ContendedAtomicLong.CACHE_LINE/Integer.BYTES;

    private final AtomicIntegerArray contendedArray;

    public ContendedAtomicInteger(final int init) {
        contendedArray = new AtomicIntegerArray(2*CACHE_LINE_INTS);

        set(init);
    }

    public int get() {
        return contendedArray.get(CACHE_LINE_INTS);
    }

    public void set(final int i) {
        contendedArray.set(CACHE_LINE_INTS, i);
    }

    public boolean compareAndSet(final int expect, final int i) {
        return contendedArray.compareAndSet(CACHE_LINE_INTS, expect, i);
    }

    public String toString() {
        return Integer.toString(get());
    }
}
