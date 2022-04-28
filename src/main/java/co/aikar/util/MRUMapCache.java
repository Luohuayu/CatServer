/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.util;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Implements a Most Recently Used cache in front of a backing map, to quickly access the last accessed result.
 *
 * @param <K> Key Type of the Map
 * @param <V> Value Type of the Map
 */
public class MRUMapCache<K, V> extends AbstractMap<K, V> {
    final Map<K, V> backingMap;
    Object cacheKey;
    V cacheValue;
    public MRUMapCache(final Map<K, V> backingMap) {
        this.backingMap = backingMap;
    }

    public int size() {return backingMap.size();}

    public boolean isEmpty() {return backingMap.isEmpty();}

    public boolean containsKey(Object key) {
        return key != null && key.equals(cacheKey) || backingMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return value != null && value == cacheValue || backingMap.containsValue(value);
    }

    public V get(Object key) {
        if (cacheKey != null && cacheKey.equals(key)) {
            return cacheValue;
        }
        cacheKey = key;
        return cacheValue = backingMap.get(key);
    }

    public V put(K key, V value) {
        cacheKey = key;
        return cacheValue = backingMap.put(key, value);
    }

    public V remove(Object key) {
        if (key != null && key.equals(cacheKey)) {
            cacheKey = null;
        }
        return backingMap.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {backingMap.putAll(m);}

    public void clear() {
        cacheKey = null;
        cacheValue = null;
        backingMap.clear();
    }

    public Set<K> keySet() {return backingMap.keySet();}

    public Collection<V> values() {return backingMap.values();}

    public Set<Entry<K, V>> entrySet() {return backingMap.entrySet();}

    /**
     * Wraps the specified map with a most recently used cache
     *
     * @param map Map to be wrapped
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> of(Map<K, V> map) {
        return new MRUMapCache<K, V>(map);
    }
}
