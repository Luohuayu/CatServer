// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.EntityThrowable;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle
{
    public CraftThrownExpBottle(final CraftServer server, final EntityExpBottle entity) {
        super(server, entity);
    }
    
    @Override
    public EntityExpBottle getHandle() {
        return (EntityExpBottle)this.entity;
    }
    
    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
