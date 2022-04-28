package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.MobEntity;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;

public abstract class CraftMob extends CraftLivingEntity implements Mob {
    public CraftMob(CraftServer server, MobEntity entity) {
        super(server, entity);
    }

    @Override
    public void setTarget(LivingEntity target) {
        MobEntity entity = getHandle();
        if (target == null) {
            entity.setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    @Override
    public CraftLivingEntity getTarget() {
        if (getHandle().getTarget() == null) return null;

        return (CraftLivingEntity) getHandle().getTarget().getBukkitEntity();
    }

    @Override
    public void setAware(boolean aware) {
        getHandle().aware = aware;
    }

    @Override
    public boolean isAware() {
        return getHandle().aware;
    }

    @Override
    public MobEntity getHandle() {
        return (MobEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMob";
    }

    @Override
    public void setLootTable(LootTable table) {
        getHandle().lootTable = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
    }

    @Override
    public LootTable getLootTable() {
        if (getHandle().lootTable == null) {
            getHandle().lootTable = getHandle().getLootTable();
        }

        NamespacedKey key = CraftNamespacedKey.fromMinecraft(getHandle().lootTable);
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        getHandle().lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return getHandle().lootTableSeed;
    }
}
