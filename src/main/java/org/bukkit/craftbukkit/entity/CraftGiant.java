// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityGiantZombie;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Giant;

public class CraftGiant extends CraftMonster implements Giant
{
    public CraftGiant(final CraftServer server, final EntityGiantZombie entity) {
        super(server, entity);
    }
    
    @Override
    public EntityGiantZombie getHandle() {
        return (EntityGiantZombie)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftGiant";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.GIANT;
    }
}
