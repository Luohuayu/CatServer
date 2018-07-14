// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityFlying;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftLivingEntity implements Flying
{
    public CraftFlying(final CraftServer server, final EntityFlying entity) {
        super(server, entity);
    }
    
    @Override
    public EntityFlying getHandle() {
        return (EntityFlying)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftFlying";
    }
}
