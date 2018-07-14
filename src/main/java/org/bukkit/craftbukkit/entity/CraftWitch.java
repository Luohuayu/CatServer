// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityWitch;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftMonster implements Witch
{
    public CraftWitch(final CraftServer server, final EntityWitch entity) {
        super(server, entity);
    }
    
    @Override
    public EntityWitch getHandle() {
        return (EntityWitch)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftWitch";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.WITCH;
    }
}
