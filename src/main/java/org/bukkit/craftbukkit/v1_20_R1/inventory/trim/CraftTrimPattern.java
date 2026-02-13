package org.bukkit.craftbukkit.v1_20_R1.inventory.trim;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class CraftTrimPattern implements TrimPattern {

    private final NamespacedKey key;
    private final net.minecraft.world.item.armortrim.TrimPattern handle;

    public CraftTrimPattern(NamespacedKey key, net.minecraft.world.item.armortrim.TrimPattern handle) {
        this.key = key;
        this.handle = handle;
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    public net.minecraft.world.item.armortrim.TrimPattern getHandle() {
        return handle;
    }
}
