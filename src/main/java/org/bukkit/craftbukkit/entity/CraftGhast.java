// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.EntityGhast;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ghast;

public class CraftGhast extends CraftFlying implements Ghast
{
    public CraftGhast(final CraftServer server, final EntityGhast entity) {
        super(server, entity);
    }
    
    @Override
    public EntityGhast getHandle() {
        return (EntityGhast)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftGhast";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.GHAST;
    }
}
