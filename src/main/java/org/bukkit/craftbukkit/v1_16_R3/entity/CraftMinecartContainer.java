package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.util.ResourceLocation;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

public abstract class CraftMinecartContainer extends CraftMinecart implements Lootable {

    public CraftMinecartContainer(CraftServer server, AbstractMinecartEntity entity) {
        super(server, entity);
    }

    @Override
    public ContainerMinecartEntity getHandle() {
        return (ContainerMinecartEntity) entity;
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public LootTable getLootTable() {
        ResourceLocation nmsTable = getHandle().lootTable;
        if (nmsTable == null) {
            return null; // return empty loot table?
        }

        NamespacedKey key = CraftNamespacedKey.fromMinecraft(nmsTable);
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    @Override
    public long getSeed() {
        return getHandle().lootTableSeed;
    }

    private void setLootTable(LootTable table, long seed) {
        ResourceLocation newKey = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
        getHandle().setLootTable(newKey, seed);
    }
}
