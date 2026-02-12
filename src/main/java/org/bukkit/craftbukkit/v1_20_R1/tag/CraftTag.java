package org.bukkit.craftbukkit.v1_20_R1.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    protected final Registry<N> registry;
    protected final TagKey<N> tag;
    //
    private HolderSet.Named<N> handle;

    public CraftTag(Registry<N> registry, TagKey<N> tag) {
        this.registry = registry;
        this.tag = tag;
        this.handle = registry.getTag(this.tag).orElseThrow();
    }

    protected HolderSet.Named<N> getHandle() {
        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag.location());
    }
}
