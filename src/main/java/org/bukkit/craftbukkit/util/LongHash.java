// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

public class LongHash
{
    public static long toLong(final int msw, final int lsw) {
        return (msw << 32) + lsw + 2147483648L;
    }
    
    public static int msw(final long l) {
        return (int)(l >> 32);
    }
    
    public static int lsw(final long l) {
        return (int)(l & -1L) + Integer.MIN_VALUE;
    }
}
