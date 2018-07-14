// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem extends CraftGolem implements IronGolem
{
    public CraftIronGolem(final CraftServer server, final EntityIronGolem entity) {
        super(server, entity);
    }
    
    @Override
    public EntityIronGolem getHandle() {
        return (EntityIronGolem)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftIronGolem";
    }
    
    @Override
    public boolean isPlayerCreated() {
        return this.getHandle().isPlayerCreated();
    }
    
    @Override
    public void setPlayerCreated(final boolean playerCreated) {
        this.getHandle().setPlayerCreated(playerCreated);
    }
    
    @Override
    public EntityType getType() {
        return EntityType.IRON_GOLEM;
    }
}
