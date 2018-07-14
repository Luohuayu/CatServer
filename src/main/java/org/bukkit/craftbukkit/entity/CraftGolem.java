// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Golem;

public class CraftGolem extends CraftCreature implements Golem
{
    public CraftGolem(final CraftServer server, final EntityGolem entity) {
        super(server, entity);
    }
    
    @Override
    public EntityGolem getHandle() {
        return (EntityGolem)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftGolem";
    }
}
