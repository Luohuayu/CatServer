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

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Avoid false cache line sharing
 *
 * Created by jcairns on 5/28/14.
 */
final class ContendedAtomicLong {

    static final int CACHE_LINE = Integer.getInteger("Intel.CacheLineSize", 64); // bytes

    private static final int CACHE_LINE_LONGS = CACHE_LINE/Long.BYTES;

    private final AtomicLongArray contendedArray;

    ContendedAtomicLong(final long init)
    {
        contendedArray = new AtomicLongArray(2*CACHE_LINE_LONGS);

        set(init);
    }

    void set(final long l) {
        contendedArray.set(CACHE_LINE_LONGS, l);
    }

    long get() {
        return contendedArray.get(CACHE_LINE_LONGS);
    }

    public String toString() {
        return Long.toString(get());
    }

    public boolean compareAndSet(final long expect, final long l) {
        return contendedArray.compareAndSet(CACHE_LINE_LONGS, expect, l);
    }
}
