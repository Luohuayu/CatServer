// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;

public class HashTreeSet<V> implements Set<V>
{
    private HashSet<V> hash;
    private TreeSet<V> tree;
    
    public HashTreeSet() {
        this.hash = new HashSet<V>();
        this.tree = new TreeSet<V>();
    }
    
    @Override
    public int size() {
        return this.hash.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.hash.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.hash.contains(o);
    }
    
    @Override
    public Iterator<V> iterator() {
        return new Iterator<V>() {
            private Iterator<V> it = HashTreeSet.this.tree.iterator();
            private V last;
            
            @Override
            public boolean hasNext() {
                return this.it.hasNext();
            }
            
            @Override
            public V next() {
                return this.last = this.it.next();
            }
            
            @Override
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                this.it.remove();
                HashTreeSet.this.hash.remove(this.last);
                this.last = null;
            }
        };
    }
    
    @Override
    public Object[] toArray() {
        return this.hash.toArray();
    }
    
    @Override
    public Object[] toArray(final Object[] a) {
        return this.hash.toArray(a);
    }
    
    @Override
    public boolean add(final V e) {
        this.hash.add(e);
        return this.tree.add(e);
    }
    
    @Override
    public boolean remove(final Object o) {
        this.hash.remove(o);
        return this.tree.remove(o);
    }
    
    @Override
    public boolean containsAll(final Collection c) {
        return this.hash.containsAll(c);
    }
    
    @Override
    public boolean addAll(final Collection c) {
        this.tree.addAll(c);
        return this.hash.addAll(c);
    }
    
    @Override
    public boolean retainAll(final Collection c) {
        this.tree.retainAll(c);
        return this.hash.retainAll(c);
    }
    
    @Override
    public boolean removeAll(final Collection c) {
        this.tree.removeAll(c);
        return this.hash.removeAll(c);
    }
    
    @Override
    public void clear() {
        this.hash.clear();
        this.tree.clear();
    }
    
    public V first() {
        return this.tree.first();
    }
}
