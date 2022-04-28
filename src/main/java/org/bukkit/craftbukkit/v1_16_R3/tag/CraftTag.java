package org.bukkit.craftbukkit.v1_16_R3.tag;

import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;

public abstract class CraftTag<N, B extends Keyed> implements Tag<B> {

    private final ITagCollection<N> registry;
    private final ResourceLocation tag;
    //
    private ITag<N> handle;

    public CraftTag(ITagCollection<N> registry, ResourceLocation tag) {
        this.registry = registry;
        this.tag = tag;
    }

    protected ITag<N> getHandle() {
        if (handle == null) {
            handle = registry.getTagOrEmpty(tag);
        }

        return handle;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(tag);
    }
}
