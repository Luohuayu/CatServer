// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityMob;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster
{
    public CraftMonster(final CraftServer server, final EntityMob entity) {
        super(server, entity);
    }
    
    @Override
    public EntityMob getHandle() {
        return (EntityMob)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftMonster";
    }
}
