// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed
{
    public CraftTNTPrimed(final CraftServer server, final EntityTNTPrimed entity) {
        super(server, entity);
    }
    
    @Override
    public float getYield() {
        return this.getHandle().yield;
    }
    
    @Override
    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }
    
    @Override
    public void setIsIncendiary(final boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }
    
    @Override
    public void setYield(final float yield) {
        this.getHandle().yield = yield;
    }
    
    @Override
    public int getFuseTicks() {
        return this.getHandle().getFuse();
    }
    
    @Override
    public void setFuseTicks(final int fuseTicks) {
        this.getHandle().setFuse(fuseTicks);
    }
    
    @Override
    public EntityTNTPrimed getHandle() {
        return (EntityTNTPrimed)this.entity;
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
    public org.bukkit.entity.Entity getSource() {
        final EntityLivingBase source = this.getHandle().getTntPlacedBy();
        if (source != null) {
            final org.bukkit.entity.Entity bukkitEntity = source.getBukkitEntity();
            if (bukkitEntity.isValid()) {
                return bukkitEntity;
            }
        }
        return null;
    }
}
