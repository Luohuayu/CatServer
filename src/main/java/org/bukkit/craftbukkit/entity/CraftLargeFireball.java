// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball extends CraftFireball implements LargeFireball
{
    public CraftLargeFireball(final CraftServer server, final EntityLargeFireball entity) {
        super(server, entity);
    }
    
    @Override
    public void setYield(final float yield) {
        super.setYield(yield);
        this.getHandle().explosionPower = (int)yield;
    }
    
    @Override
    public EntityLargeFireball getHandle() {
        return (EntityLargeFireball)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftLargeFireball";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.FIREBALL;
    }
}
