package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.item.PrimedTnt;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, PrimedTnt entity) {
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
    public PrimedTnt getHandle() {
        return (PrimedTnt) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    @Override
    public Entity getSource() {
        net.minecraft.world.entity.LivingEntity source = getHandle().getOwner();

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
