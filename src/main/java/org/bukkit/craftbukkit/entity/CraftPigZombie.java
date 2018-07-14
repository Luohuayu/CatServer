// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityPigZombie;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie
{
    public CraftPigZombie(final CraftServer server, final EntityPigZombie entity) {
        super(server, entity);
    }
    
    @Override
    public int getAnger() {
        return this.getHandle().angerLevel;
    }
    
    @Override
    public void setAnger(final int level) {
        this.getHandle().angerLevel = level;
    }
    
    @Override
    public void setAngry(final boolean angry) {
        this.setAnger(angry ? 400 : 0);
    }
    
    @Override
    public boolean isAngry() {
        return this.getAnger() > 0;
    }
    
    @Override
    public EntityPigZombie getHandle() {
        return (EntityPigZombie)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftPigZombie";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.PIG_ZOMBIE;
    }
}
