// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.boss.EntityWither;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither
{
    public CraftWither(final CraftServer server, final EntityWither entity) {
        super(server, entity);
    }
    
    @Override
    public EntityWither getHandle() {
        return (EntityWither)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftWither";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }
}
