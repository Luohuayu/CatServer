package catserver.server.utils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class CatTreeSet<E> extends TreeSet<E> {
    private ConcurrentSkipListSet<E> m;
    private static final Object PRESENT = new Object();

    CatTreeSet(ConcurrentSkipListSet<E> m) {
        this.m = m;
    }

    public CatTreeSet() {
        this(new ConcurrentSkipListSet<E>());
    }

    public CatTreeSet(Comparator<? super E> comparator) {
        this(new ConcurrentSkipListSet<>(comparator));
    }

    public CatTreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public CatTreeSet(SortedSet<E> s) {
        this(s.comparator());
        addAll(s);
    }

    public Iterator<E> iterator() {
        return m.iterator();
    }

    public Iterator<E> descendingIterator() {
        return m.descendingIterator();
    }

    public NavigableSet<E> descendingSet() {
        return m.descendingSet();
    }

    public int size() {
        return m.size();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean contains(Object o) {
        return m.contains(o);
    }

    public boolean add(E e) {
        return m.add(e);
    }

    public boolean remove(Object o) {
        return m.remove(o);
    }

    public void clear() {
        m.clear();
    }

    public  boolean addAll(Collection<? extends E> c) {
        // Use linear-time version if applicable
        return m.addAll(c);
    }

    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
                                  E toElement,   boolean toInclusive) {
        return m.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return m.headSet(toElement, inclusive);
    }

    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return m.tailSet(fromElement, inclusive);
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    public Comparator<? super E> comparator() {
        return m.comparator();
    }
    public E first() {
        return m.first();
    }

    public E last() {
        return m.last();
    }

    public E lower(E e) {
        return m.lower(e);
    }

    public E floor(E e) {
        return m.floor(e);
    }

    public E ceiling(E e) {
        return m.ceiling(e);
    }

    public E higher(E e) {
        return m.higher(e);
    }

    public E pollFirst() {
        return m.pollFirst();
    }

    public E pollLast() {
        return m.pollLast();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        return m.clone();
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        // Write out any hidden stuff
        s.defaultWriteObject();

        // Write out Comparator
        s.writeObject(m.comparator());

        // Write out size
        s.writeInt(m.size());

        // Write out all elements in the proper order.
        for (E e : m)
            s.writeObject(e);
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("PendingTreeSet");
    }

    public Spliterator<E> spliterator() {
        return m.spliterator();
    }

    private static final long serialVersionUID = -2479143000061671589L;
}
