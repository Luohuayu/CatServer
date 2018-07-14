// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.effect.EntityLightningBolt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike
{
    public CraftLightningStrike(final CraftServer server, final EntityLightningBolt entity) {
        super(server, entity);
    }
    
    @Override
    public boolean isEffect() {
        return ((EntityLightningBolt)super.getHandle()).effectOnly;
    }
    
    @Override
    public EntityLightningBolt getHandle() {
        return (EntityLightningBolt)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftLightningStrike";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}
