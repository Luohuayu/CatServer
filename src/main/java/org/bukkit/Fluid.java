package org.bukkit;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a fluid type.
 */
public enum Fluid implements Keyed {

    /**
     * Stationary water.
     */
    WATER,
    /**
     * Flowing water.
     */
    FLOWING_WATER,
    /**
     * Stationary lava.
     */
    LAVA,
    /**
     * Flowing lava.
     */
    FLOWING_LAVA;

    private final NamespacedKey key;

    private Fluid() {
        this.key = NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
    }

    // CatServer start
    private Fluid(NamespacedKey key) {
        this.key = key == null ? NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT)) : key;
    }
    // CatServer end

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
