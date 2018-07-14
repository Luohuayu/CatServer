// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.List;
import java.util.AbstractList;

public class UnsafeList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable
{
    private static final long serialVersionUID = 8683452581112892191L;
    private transient Object[] data;
    private int size;
    private int initialCapacity;
    private Iterator[] iterPool;
    private int maxPool;
    private int poolCounter;
    
    public UnsafeList(int capacity, final int maxIterPool) {
        this.iterPool = new Iterator[1];
        if (capacity < 0) {
            capacity = 32;
        }
        final int rounded = Integer.highestOneBit(capacity - 1) << 1;
        this.data = new Object[rounded];
        this.initialCapacity = rounded;
        this.maxPool = maxIterPool;
        this.iterPool[0] = new Itr();
    }
    
    public UnsafeList(final int capacity) {
        this(capacity, 5);
    }
    
    public UnsafeList() {
        this(32);
    }
    
    @Override
    public E get(final int index) {
        this.rangeCheck(index);
        return (E)this.data[index];
    }
    
    public E unsafeGet(final int index) {
        return (E)this.data[index];
    }
    
    @Override
    public E set(final int index, final E element) {
        this.rangeCheck(index);
        final E old = (E)this.data[index];
        this.data[index] = element;
        return old;
    }
    
    @Override
    public boolean add(final E element) {
        this.growIfNeeded();
        this.data[this.size++] = element;
        return true;
    }
    
    @Override
    public void add(final int index, final E element) {
        this.growIfNeeded();
        System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
        this.data[index] = element;
        ++this.size;
    }
    
    @Override
    public E remove(final int index) {
        this.rangeCheck(index);
        final E old = (E)this.data[index];
        final int movedCount = this.size - index - 1;
        if (movedCount > 0) {
            System.arraycopy(this.data, index + 1, this.data, index, movedCount);
        }
        this.data[--this.size] = null;
        return old;
    }
    
    @Override
    public boolean remove(final Object o) {
        final int index = this.indexOf(o);
        if (index >= 0) {
            this.remove(index);
            return true;
        }
        return false;
    }
    
    @Override
    public int indexOf(final Object o) {
        for (int i = 0; i < this.size; ++i) {
            if (o == this.data[i] || o.equals(this.data[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) >= 0;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        if (this.data.length > this.initialCapacity << 3) {
            this.data = new Object[this.initialCapacity];
        }
        else {
            for (int i = 0; i < this.data.length; ++i) {
                this.data[i] = null;
            }
        }
    }
    
    public void trimToSize() {
        final int old = this.data.length;
        final int rounded = Integer.highestOneBit(this.size - 1) << 1;
        if (rounded < old) {
            this.data = Arrays.copyOf(this.data, rounded);
        }
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final UnsafeList<E> copy = (UnsafeList<E>)super.clone();
        copy.data = Arrays.copyOf(this.data, this.size);
        copy.size = this.size;
        copy.initialCapacity = this.initialCapacity;
        (copy.iterPool = new Iterator[1])[0] = new Itr();
        copy.maxPool = this.maxPool;
        copy.poolCounter = 0;
        return copy;
    }
    
    @Override
    public Iterator<E> iterator() {
        Iterator[] iterPool;
        for (int length = (iterPool = this.iterPool).length, i = 0; i < length; ++i) {
            final Iterator iter = iterPool[i];
            if (!((Itr)iter).valid) {
                final Itr iterator = (Itr)iter;
                iterator.reset();
                return iterator;
            }
        }
        if (this.iterPool.length < this.maxPool) {
            final Iterator[] newPool = new Iterator[this.iterPool.length + 1];
            System.arraycopy(this.iterPool, 0, newPool, 0, this.iterPool.length);
            this.iterPool = newPool;
            return (Iterator<E>)(this.iterPool[this.iterPool.length - 1] = new Itr());
        }
        this.poolCounter = ++this.poolCounter % this.iterPool.length;
        return (Iterator<E>)(this.iterPool[this.poolCounter] = new Itr());
    }
    
    private void rangeCheck(final int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
    }
    
    private void growIfNeeded() {
        if (this.size == this.data.length) {
            final Object[] newData = new Object[this.data.length << 1];
            System.arraycopy(this.data, 0, newData, 0, this.size);
            this.data = newData;
        }
    }
    
    private void writeObject(final ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
        os.writeInt(this.size);
        os.writeInt(this.initialCapacity);
        for (int i = 0; i < this.size; ++i) {
            os.writeObject(this.data[i]);
        }
        os.writeInt(this.maxPool);
    }
    
    private void readObject(final ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();
        this.size = is.readInt();
        this.initialCapacity = is.readInt();
        this.data = new Object[Integer.highestOneBit(this.size - 1) << 1];
        for (int i = 0; i < this.size; ++i) {
            this.data[i] = is.readObject();
        }
        this.maxPool = is.readInt();
        (this.iterPool = new Iterator[1])[0] = new Itr();
    }
    
    public class Itr implements Iterator<E>
    {
        int index;
        int lastRet;
        int expectedModCount;
        public boolean valid;
        
        public Itr() {
            this.lastRet = -1;
            this.expectedModCount = UnsafeList.this.modCount;
            this.valid = true;
        }
        
        public void reset() {
            this.index = 0;
            this.lastRet = -1;
            this.expectedModCount = UnsafeList.this.modCount;
            this.valid = true;
        }
        
        @Override
        public boolean hasNext() {
            return this.valid = (this.index != UnsafeList.this.size);
        }
        
        @Override
        public E next() {
            if (UnsafeList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            final int i = this.index;
            if (i >= UnsafeList.this.size) {
                throw new NoSuchElementException();
            }
            if (i >= UnsafeList.this.data.length) {
                throw new ConcurrentModificationException();
            }
            this.index = i + 1;
            final Object[] access$2 = UnsafeList.this.data;
            final int lastRet = i;
            this.lastRet = lastRet;
            return (E)access$2[lastRet];
        }
        
        @Override
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            if (UnsafeList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            try {
                UnsafeList.this.remove(this.lastRet);
                this.index = this.lastRet;
                this.lastRet = -1;
                this.expectedModCount = UnsafeList.this.modCount;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
