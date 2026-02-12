package org.bukkit.craftbukkit.v1_20_R1.util;

import org.bukkit.util.CachedServerIcon;

public class CraftIconCache implements CachedServerIcon {
    public final byte[] value;

    public CraftIconCache(final byte[] value) {
        this.value = value;
    }

    public String getData() {
        if (value == null) {
            return null;
        }
        return "data:image/png;base64," + new String(java.util.Base64.getEncoder().encode(value), java.nio.charset.StandardCharsets.UTF_8);
    } // Paper
}
