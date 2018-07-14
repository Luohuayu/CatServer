// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWaterMob;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftLivingEntity implements WaterMob
{
    public CraftWaterMob(final CraftServer server, final EntityWaterMob entity) {
        super(server, entity);
    }
    
    @Override
    public EntityWaterMob getHandle() {
        return (EntityWaterMob)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftWaterMob";
    }
}
