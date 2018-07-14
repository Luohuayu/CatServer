// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntitySquid;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftWaterMob implements Squid
{
    public CraftSquid(final CraftServer server, final EntitySquid entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySquid getHandle() {
        return (EntitySquid)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSquid";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SQUID;
    }
}
