// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.EntityThrowable;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;

public class CraftEgg extends CraftProjectile implements Egg
{
    public CraftEgg(final CraftServer server, final EntityEgg entity) {
        super(server, entity);
    }
    
    @Override
    public EntityEgg getHandle() {
        return (EntityEgg)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEgg";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.EGG;
    }
}
