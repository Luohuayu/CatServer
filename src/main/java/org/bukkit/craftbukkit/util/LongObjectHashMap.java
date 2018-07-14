// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Collection;
import java.util.Set;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.io.Serializable;

public class LongObjectHashMap<V> implements Cloneable, Serializable
{
    static final long serialVersionUID = 2841537710170573815L;
    private static final long EMPTY_KEY = Long.MIN_VALUE;
    private static final int BUCKET_SIZE = 4096;
    private transient long[][] keys;
    private transient Object[][] values;
    private transient int modCount;
    private transient int size;
    
    public LongObjectHashMap() {
        this.initialize();
    }
    
    public LongObjectHashMap(final Map<? extends Long, ? extends V> map) {
        this();
        this.putAll(map);
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public boolean containsKey(final long key) {
        return this.get(key) != null;
    }
    
    public boolean containsValue(final Object value) {
        for (final V val : this.values()) {
            if (val == value || val.equals(value)) {
                return true;
            }
        }
        return false;
    }
    
    public V get(final long key) {
        final int index = (int)(this.keyIndex(key) & 0xFFFL);
        final long[] inner = this.keys[index];
        if (inner == null) {
            return null;
        }
        for (int i = 0; i < inner.length; ++i) {
            final long innerKey = inner[i];
            if (innerKey == Long.MIN_VALUE) {
                return null;
            }
            if (innerKey == key) {
                return (V)this.values[index][i];
            }
        }
        return null;
    }
    
    public V put(final long key, final V value) {
        final int index = (int)(this.keyIndex(key) & 0xFFFL);
        long[] innerKeys = this.keys[index];
        Object[] innerValues = this.values[index];
        ++this.modCount;
        if (innerKeys == null) {
            innerKeys = (this.keys[index] = new long[8]);
            Arrays.fill(innerKeys, Long.MIN_VALUE);
            innerValues = (this.values[index] = (V[]) new Object[8]);
            innerKeys[0] = key;
            innerValues[0] = value;
            ++this.size;
        }
        else {
            int i;
            for (i = 0; i < innerKeys.length; ++i) {
                if (innerKeys[i] == Long.MIN_VALUE) {
                    ++this.size;
                    innerKeys[i] = key;
                    innerValues[i] = value;
                    return null;
                }
                if (innerKeys[i] == key) {
                    final V oldValue = (V)innerValues[i];
                    innerKeys[i] = key;
                    innerValues[i] = value;
                    return oldValue;
                }
            }
            innerKeys = (this.keys[index] = Arrays.copyOf(innerKeys, i << 1));
            Arrays.fill(innerKeys, i, innerKeys.length, Long.MIN_VALUE);
            innerValues = (this.values[index] = (V[]) Arrays.copyOf(innerValues, i << 1));
            innerKeys[i] = key;
            innerValues[i] = value;
            ++this.size;
        }
        return null;
    }
    
    public V remove(final long key) {
        final int index = (int)(this.keyIndex(key) & 0xFFFL);
        final long[] inner = this.keys[index];
        if (inner == null) {
            return null;
        }
        for (int i = 0; i < inner.length && inner[i] != Long.MIN_VALUE; ++i) {
            if (inner[i] == key) {
                final V value = (V)this.values[index][i];
                ++i;
                while (i < inner.length && inner[i] != Long.MIN_VALUE) {
                    inner[i - 1] = inner[i];
                    this.values[index][i - 1] = this.values[index][i];
                    ++i;
                }
                inner[i - 1] = Long.MIN_VALUE;
                this.values[index][i - 1] = null;
                --this.size;
                ++this.modCount;
                return value;
            }
        }
        return null;
    }
    
    public void putAll(final Map<? extends Long, ? extends V> map) {
        for (final java.util.Map.Entry<? extends Long, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        ++this.modCount;
        this.size = 0;
        Arrays.fill(this.keys, null);
        Arrays.fill(this.values, null);
    }
    
    public Set<Long> keySet() {
        return new KeySet();
    }
    
    public Collection<V> values() {
        return new ValueCollection();
    }
    
    @Deprecated
    public Set<Map.Entry<Long, V>> entrySet() {
        final HashSet<Map.Entry<Long, V>> set = new HashSet<Map.Entry<Long, V>>();
        for (final long key : this.keySet()) {
            set.add(new Entry(key, this.get(key)));
        }
        return set;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final LongObjectHashMap clone = (LongObjectHashMap)super.clone();
        clone.clear();
        clone.initialize();
        for (final long key : this.keySet()) {
            final V value = this.get(key);
            clone.put(key, value);
        }
        return clone;
    }
    
    private void initialize() {
        this.keys = new long[4096][];
        this.values = new Object[4096][];
    }
    
    private long keyIndex(long key) {
        key ^= key >>> 33;
        key *= -49064778989728563L;
        key ^= key >>> 33;
        key *= -4265267296055464877L;
        key ^= key >>> 33;
        return key;
    }
    
    private void writeObject(final ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        for (final long key : this.keySet()) {
            final V value = this.get(key);
            outputStream.writeLong(key);
            outputStream.writeObject(value);
        }
        outputStream.writeLong(Long.MIN_VALUE);
        outputStream.writeObject(null);
    }
    
    private void readObject(final ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        inputStream.defaultReadObject();
        this.initialize();
        while (true) {
            final long key = inputStream.readLong();
            final V value = (V)inputStream.readObject();
            if (key == Long.MIN_VALUE && value == null) {
                break;
            }
            this.put(key, value);
        }
    }
    
    private class Entry implements Map.Entry<Long, V>
    {
        private final Long key;
        private V value;
        
        Entry(final long k, final V v) {
            this.key = k;
            this.value = v;
        }
        
        @Override
        public Long getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public V setValue(final V v) {
            final V old = this.value;
            this.value = v;
            LongObjectHashMap.this.put(this.key, v);
            return old;
        }
    }
    
    private class KeyIterator implements Iterator<Long>
    {
        final ValueIterator iterator;
        
        public KeyIterator() {
            this.iterator = new ValueIterator();
        }
        
        @Override
        public void remove() {
            this.iterator.remove();
        }
        
        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Override
        public Long next() {
            this.iterator.next();
            return this.iterator.prevKey;
        }
    }
    
    private class ValueIterator implements Iterator<V>
    {
        private int count;
        private int index;
        private int innerIndex;
        private int expectedModCount;
        private long lastReturned;
        long prevKey;
        V prevValue;
        
        ValueIterator() {
            this.lastReturned = Long.MIN_VALUE;
            this.prevKey = Long.MIN_VALUE;
            this.expectedModCount = LongObjectHashMap.this.modCount;
        }
        
        @Override
        public boolean hasNext() {
            return this.count < LongObjectHashMap.this.size;
        }
        
        @Override
        public void remove() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (this.lastReturned == Long.MIN_VALUE) {
                throw new IllegalStateException();
            }
            --this.count;
            LongObjectHashMap.this.remove(this.lastReturned);
            this.lastReturned = Long.MIN_VALUE;
            this.expectedModCount = LongObjectHashMap.this.modCount;
        }
        
        @Override
        public V next() {
            if (LongObjectHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final long[][] keys = LongObjectHashMap.this.keys;
            ++this.count;
            if (this.prevKey != Long.MIN_VALUE) {
                ++this.innerIndex;
            }
            while (this.index < keys.length) {
                if (keys[this.index] != null) {
                    if (this.innerIndex < keys[this.index].length) {
                        final long key = keys[this.index][this.innerIndex];
                        final V value = (V)LongObjectHashMap.this.values[this.index][this.innerIndex];
                        if (key != Long.MIN_VALUE) {
                            this.lastReturned = key;
                            this.prevKey = key;
                            return this.prevValue = value;
                        }
                    }
                    this.innerIndex = 0;
                }
                ++this.index;
            }
            throw new NoSuchElementException();
        }
    }
    
    private class KeySet extends AbstractSet<Long>
    {
        @Override
        public void clear() {
            LongObjectHashMap.this.clear();
        }
        
        @Override
        public int size() {
            return LongObjectHashMap.this.size();
        }
        
        @Override
        public boolean contains(final Object key) {
            return key instanceof Long && LongObjectHashMap.this.containsKey((Long)key);
        }
        
        @Override
        public boolean remove(final Object key) {
            return LongObjectHashMap.this.remove((Long)key) != null;
        }
        
        @Override
        public Iterator<Long> iterator() {
            return new KeyIterator();
        }
    }
    
    private class ValueCollection extends AbstractCollection<V>
    {
        @Override
        public void clear() {
            LongObjectHashMap.this.clear();
        }
        
        @Override
        public int size() {
            return LongObjectHashMap.this.size();
        }
        
        @Override
        public boolean contains(final Object value) {
            return LongObjectHashMap.this.containsValue(value);
        }
        
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
    }
}
