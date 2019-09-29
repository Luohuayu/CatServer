package com.conversantmedia.util.concurrent;

/*
 * #%L
 * Conversant Disruptor
 * ~~
 * Conversantmedia.com © 2018, Conversant, Inc. Conversant® is a trademark of Conversant, Inc.
 * John Cairns © 2018
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
 * Created by jcairns on 7/12/2018
 */
final class Capacity {

    public static final int MAX_POWER2 = (1<<30);

    /**
     * return the next power of two after @param capacity or
     * capacity if it is already
     */
    public static int getCapacity(int capacity) {
        int c = 1;
        if(capacity >= MAX_POWER2) {
            c = MAX_POWER2;
        } else {
            while(c < capacity) c <<= 1;
        }

        if(isPowerOf2(c)) {
            return c;
        } else {
            throw new RuntimeException("Capacity is not a power of 2.");
        }
    }

    /*
     * define power of 2 slightly strangely to include 1, 
     *  i.e. capacity 1 is allowed
     */
    private static final boolean isPowerOf2(final int p) {
        // thanks mcheng for the suggestion
        return (p & (p - 1)) == 0;
    }
}
