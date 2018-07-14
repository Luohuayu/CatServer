// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftFireball implements SmallFireball
{
    public CraftSmallFireball(final CraftServer server, final EntitySmallFireball entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySmallFireball getHandle() {
        return (EntitySmallFireball)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSmallFireball";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SMALL_FIREBALL;
    }
}
