// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.EntityThrowable;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Snowball;

public class CraftSnowball extends CraftProjectile implements Snowball
{
    public CraftSnowball(final CraftServer server, final EntitySnowball entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySnowball getHandle() {
        return (EntitySnowball)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSnowball";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SNOWBALL;
    }
}
