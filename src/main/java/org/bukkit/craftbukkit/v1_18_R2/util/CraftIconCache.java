package org.bukkit.craftbukkit.v1_18_R2.util;

import org.bukkit.util.CachedServerIcon;

public class CraftIconCache implements CachedServerIcon {
    public final String value;

    public CraftIconCache(final String value) {
        this.value = value;
    }
}
