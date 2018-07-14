// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityDragonFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.DragonFireball;

public class CraftDragonFireball extends CraftFireball implements DragonFireball
{
    public CraftDragonFireball(final CraftServer server, final EntityDragonFireball entity) {
        super(server, entity);
    }
    
    @Override
    public EntityType getType() {
        return EntityType.DRAGON_FIREBALL;
    }
}
