package catserver.server.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class CatEntityRegistry<K, V> extends RegistryNamespaced<K, V> {
    private final RegistryNamespaced<K, V> REGISTRY = new RegistryNamespaced<K, V>();

    public void register(int id, K key, V value) {
        REGISTRY.register(id, key, value);
    }

    @Nullable
    public V getObject(@Nullable K name)
    {
        EntityEntry entry = net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES.getValue((ResourceLocation) name);
        return entry == null ? REGISTRY.getObject(name) : (V) entry.getEntityClass();
    }

    @Nullable
    public K getNameForObject(V value)
    {
        EntityEntry entry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry((Class<? extends Entity>) value);
        return entry == null ? REGISTRY.getNameForObject(value) : (K) entry.getRegistryName();
    }

    public boolean containsKey(K key)
    {
        return net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES.getValue((ResourceLocation) key) != null || REGISTRY.containsKey(key);
    }

    public int getIDForObject(@Nullable V value)
    {
        EntityEntry entry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry((Class<? extends Entity>) value);
        return entry == null ? REGISTRY.getIDForObject(value) : net.minecraftforge.registries.GameData.getEntityRegistry().getID(entry);
    }

    @Nullable
    public V getObjectById(int id)
    {
        EntityEntry entry = net.minecraftforge.registries.GameData.getEntityRegistry().getValue(id);
        return entry == null ? REGISTRY.getObjectById(id) : (V) entry;
    }

    public Iterator<V> iterator()
    {
        List<V> list = new ArrayList<>();
        for (EntityEntry value : net.minecraftforge.registries.GameData.getEntityRegistry().getValues()) list.add((V) value);
        for (V value : REGISTRY) list.add(value);
        return list.iterator();
    }
}
