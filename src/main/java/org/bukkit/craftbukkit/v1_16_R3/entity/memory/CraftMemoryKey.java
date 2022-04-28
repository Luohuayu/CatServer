package org.bukkit.craftbukkit.v1_16_R3.entity.memory;

import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.registry.Registry;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.memory.MemoryKey;

public final class CraftMemoryKey {

    private CraftMemoryKey() {}

    public static <T, U> MemoryModuleType<U> fromMemoryKey(MemoryKey<T> memoryKey) {
        return (MemoryModuleType<U>) Registry.MEMORY_MODULE_TYPE.get(CraftNamespacedKey.toMinecraft(memoryKey.getKey()));
    }

    public static <T, U> MemoryKey<U> toMemoryKey(MemoryModuleType<T> memoryModuleType) {
        return MemoryKey.getByKey(CraftNamespacedKey.fromMinecraft(Registry.MEMORY_MODULE_TYPE.getKey(memoryModuleType)));
    }
}
