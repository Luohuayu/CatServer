package org.spigotmc;

import gnu.trove.strategy.HashingStrategy;

class CaseInsensitiveHashingStrategy implements HashingStrategy {

    static final CaseInsensitiveHashingStrategy INSTANCE = new CaseInsensitiveHashingStrategy();

    @Override
    public int computeHashCode(Object object) {
        return ((String) object).toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object o1, Object o2) {
        return o1.equals(o2) || (o1 instanceof String && o2 instanceof String && ((String) o1).toLowerCase().equals(((String) o2).toLowerCase()));
    }
}
