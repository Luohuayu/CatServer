package co.aikar.util;

import com.google.common.collect.ForwardingMap;
import java.util.HashMap;
import java.util.Map;

public class Counter <T> extends ForwardingMap<T, Long> {
    private final Map<T, Long> counts = new HashMap<>();

    public long decrement(T key) {
        return increment(key, -1);
    }
    public long increment(T key) {
        return increment(key, 1);
    }
    public long decrement(T key, long amount) {
        return decrement(key, -amount);
    }
    public long increment(T key, long amount) {
        Long count = this.getCount(key);
        count += amount;
        this.counts.put(key, count);
        return count;
    }

    public long getCount(T key) {
        return this.counts.getOrDefault(key, 0L);
    }

    @Override
    protected Map<T, Long> delegate() {
        return this.counts;
    }
}
