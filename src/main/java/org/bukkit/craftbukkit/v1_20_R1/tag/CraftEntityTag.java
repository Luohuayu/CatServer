package org.bukkit.craftbukkit.v1_20_R1.tag;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;

public class CraftEntityTag extends CraftTag<net.minecraft.world.entity.EntityType<?>, EntityType> {

    public CraftEntityTag(net.minecraft.core.Registry<net.minecraft.world.entity.EntityType<?>> registry, TagKey<net.minecraft.world.entity.EntityType<?>> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(EntityType entity) {
        return registry.getHolderOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, CraftNamespacedKey.toMinecraft(entity.getKey()))).is(tag);
    }

    @Override
    public Set<EntityType> getValues() {
        return getHandle().stream().map((nms) -> Registry.ENTITY_TYPE.get(CraftNamespacedKey.fromMinecraft(net.minecraft.world.entity.EntityType.getKey(nms.value())))).filter(Objects::nonNull).collect(Collectors.toUnmodifiableSet());
    }
}
