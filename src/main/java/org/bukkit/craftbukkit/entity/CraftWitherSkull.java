// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull
{
    public CraftWitherSkull(final CraftServer server, final EntityWitherSkull entity) {
        super(server, entity);
    }
    
    @Override
    public void setCharged(final boolean charged) {
        this.getHandle().setInvulnerable(charged);
    }
    
    @Override
    public boolean isCharged() {
        return this.getHandle().isInvulnerable();
    }
    
    @Override
    public EntityWitherSkull getHandle() {
        return (EntityWitherSkull)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftWitherSkull";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
