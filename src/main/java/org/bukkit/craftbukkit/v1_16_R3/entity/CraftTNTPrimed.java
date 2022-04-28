package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, TNTEntity entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return getHandle().yield;
    }

    @Override
    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        getHandle().yield = yield;
    }

    @Override
    public int getFuseTicks() {
        return getHandle().getFuse();
    }

    @Override
    public void setFuseTicks(int fuseTicks) {
        getHandle().setFuse(fuseTicks);
    }

    @Override
    public TNTEntity getHandle() {
        return (TNTEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    @Override
    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    @Override
    public Entity getSource() {
        LivingEntity source = getHandle().getOwner();

        return (source != null) ? source.getBukkitEntity() : null;
    }

    @Override
    public void setSource(Entity source) {
        if (source instanceof LivingEntity) {
            getHandle().owner = ((CraftLivingEntity) source).getHandle();
        } else {
            getHandle().owner = null;
        }
    }
}
