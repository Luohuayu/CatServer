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

import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Allows you to pass a Loader function that when a key is accessed that doesn't exists,
 * automatically loads the entry into the map by calling the loader Function.
 *
 * .get() Will only return null if the Loader can return null.
 *
 * You may pass any backing Map to use.
 *
 * This class is not thread safe and should be wrapped with Collections.synchronizedMap on the OUTSIDE of the LoadingMap if needed.
 *
 * Do not wrap the backing map with Collections.synchronizedMap.
 *
 * @param <K> Key
 * @param <V> Value
 */
public class LoadingMap <K, V> extends AbstractMap<K, V> {
    private final Map<K, V> backingMap;
    private final Function<K, V> loader;

    /**
     * Initializes an auto loading map using specified loader and backing map
     * @param backingMap Map to wrap
     * @param loader Loader
     */
    public LoadingMap(Map<K, V> backingMap, Function<K, V> loader) {
        this.backingMap = backingMap;
        this.loader = loader;
    }

    /**
     * Creates a new LoadingMap with the specified map and loader
     *
     * @param backingMap Actual map being used.
     * @param loader Loader to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> of(Map<K, V> backingMap, Function<K, V> loader) {
        return new LoadingMap<>(backingMap, loader);
    }

    /**
     * Creates a LoadingMap with an auto instantiating loader.
     *
     * Will auto construct class of of Value when not found
     *
     * Since this uses Reflection, It is more effecient to define your own static loader
     * than using this helper, but if performance is not critical, this is easier.
     *
     * @param backingMap Actual map being used.
     * @param keyClass Class used for the K generic
     * @param valueClass Class used for the V generic
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newAutoMap(Map<K, V> backingMap, final Class<? extends K> keyClass,
                                              final Class<? extends V> valueClass) {
        return new LoadingMap<>(backingMap, new AutoInstantiatingLoader<>(keyClass, valueClass));
    }
    /**
     * Creates a LoadingMap with an auto instantiating loader.
     *
     * Will auto construct class of of Value when not found
     *
     * Since this uses Reflection, It is more effecient to define your own static loader
     * than using this helper, but if performance is not critical, this is easier.
     *
     * @param backingMap Actual map being used.
     * @param valueClass Class used for the V generic
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newAutoMap(Map<K, V> backingMap,
                                              final Class<? extends V> valueClass) {
        return newAutoMap(backingMap, null, valueClass);
    }

    /**
     * @see #newAutoMap
     *
     * new Auto initializing map using a HashMap.
     *
     * @param keyClass Class used for the K generic
     * @param valueClass Class used for the V generic
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends K> keyClass, final Class<? extends V> valueClass) {
        return newAutoMap(new HashMap<>(), keyClass, valueClass);
    }

    /**
     * @see #newAutoMap
     *
     * new Auto initializing map using a HashMap.
     *
     * @param valueClass Class used for the V generic
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends V> valueClass) {
        return newHashAutoMap(null, valueClass);
    }

    /**
     * @see #newAutoMap
     *
     * new Auto initializing map using a HashMap.
     *
     * @param keyClass Class used for the K generic
     * @param valueClass Class used for the V generic
     * @param initialCapacity Initial capacity to use
     * @param loadFactor Load factor to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends K> keyClass, final Class<? extends V> valueClass, int initialCapacity, float loadFactor) {
        return newAutoMap(new HashMap<>(initialCapacity, loadFactor), keyClass, valueClass);
    }

    /**
     * @see #newAutoMap
     *
     * new Auto initializing map using a HashMap.
     *
     * @param valueClass Class used for the V generic
     * @param initialCapacity Initial capacity to use
     * @param loadFactor Load factor to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return  Map that auto instantiates on .get()
     */
    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends V> valueClass, int initialCapacity, float loadFactor) {
        return newHashAutoMap(null, valueClass, initialCapacity, loadFactor);
    }

    /**
     * Initializes an auto loading map using a HashMap
     *
     * @param loader Loader to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> newHashMap(Function<K, V> loader) {
        return new LoadingMap<>(new HashMap<>(), loader);
    }

    /**
     * Initializes an auto loading map using a HashMap
     *
     * @param loader Loader to use
     * @param initialCapacity Initial capacity to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> newHashMap(Function<K, V> loader, int initialCapacity) {
        return new LoadingMap<>(new HashMap<>(initialCapacity), loader);
    }
    /**
     * Initializes an auto loading map using a HashMap
     *
     * @param loader Loader to use
     * @param initialCapacity Initial capacity to use
     * @param loadFactor Load factor to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> newHashMap(Function<K, V> loader, int initialCapacity, float loadFactor) {
        return new LoadingMap<>(new HashMap<>(initialCapacity, loadFactor), loader);
    }

    /**
     * Initializes an auto loading map using an Identity HashMap
     *
     * @param loader Loader to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> newIdentityHashMap(Function<K, V> loader) {
        return new LoadingMap<>(new IdentityHashMap<>(), loader);
    }

    /**
     * Initializes an auto loading map using an Identity HashMap
     *
     * @param loader Loader to use
     * @param initialCapacity Initial capacity to use
     * @param <K> Key Type of the Map
     * @param <V> Value Type of the Map
     * @return Map
     */
    public static <K, V> Map<K, V> newIdentityHashMap(Function<K, V> loader, int initialCapacity) {
        return new LoadingMap<>(new IdentityHashMap<>(initialCapacity), loader);
    }

    @Override
    public int size() {return backingMap.size();}

    @Override
    public boolean isEmpty() {return backingMap.isEmpty();}

    @Override
    public boolean containsKey(Object key) {return backingMap.containsKey(key);}

    @Override
    public boolean containsValue(Object value) {return backingMap.containsValue(value);}

    @Override
    public V get(Object key) {
        V v = backingMap.get(key);
        if (v != null) {
            return v;
        }
        return backingMap.computeIfAbsent((K) key, loader);
    }

    public V put(K key, V value) {return backingMap.put(key, value);}

    @Override
    public V remove(Object key) {return backingMap.remove(key);}

    public void putAll(Map<? extends K, ? extends V> m) {backingMap.putAll(m);}

    @Override
    public void clear() {backingMap.clear();}

    @Override
    public Set<K> keySet() {return backingMap.keySet();}

    @Override
    public Collection<V> values() {return backingMap.values();}

    @Override
    public boolean equals(Object o) {return backingMap.equals(o);}

    @Override
    public int hashCode() {return backingMap.hashCode();}

    @Override
    public Set<Entry<K, V>> entrySet() {
        return backingMap.entrySet();
    }

    public LoadingMap<K, V> clone() {
        return new LoadingMap<>(backingMap, loader);
    }

    private static class AutoInstantiatingLoader<K, V> implements Function<K, V> {
        final Constructor<? extends V> constructor;
        private final Class<? extends V> valueClass;

        AutoInstantiatingLoader(Class<? extends K> keyClass, Class<? extends V> valueClass) {
            try {
                this.valueClass = valueClass;
                if (keyClass != null) {
                    constructor = valueClass.getConstructor(keyClass);
                } else {
                    constructor = null;
                }
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                    valueClass.getName() + " does not have a constructor for " + (keyClass != null ? keyClass.getName() : null));
            }
        }

        @Override
        public V apply(K input) {
            try {
                return (constructor != null ? constructor.newInstance(input) : valueClass.newInstance());
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }

    /**
     * Due to java stuff, you will need to cast it to (Function) for some cases
     *
     * @param <T> Type
     */
    public abstract static class Feeder <T> implements Function<T, T> {
        @Override
        public T apply(Object input) {
            return apply();
        }

        public abstract T apply();
    }
}
