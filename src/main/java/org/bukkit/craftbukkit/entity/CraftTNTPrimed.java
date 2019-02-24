package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, EntityTNTPrimed entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().yield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return getHandle().getFuse();
    }

    public void setFuseTicks(int fuseTicks) {
        getHandle().setFuse(fuseTicks);
    }

    @Override
    public EntityTNTPrimed getHandle() {
        return (EntityTNTPrimed) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    public Entity getSource() {
        EntityLivingBase source = getHandle().getTntPlacedBy();

        return (source != null) ? source.getBukkitEntity() : null;
    }
}
