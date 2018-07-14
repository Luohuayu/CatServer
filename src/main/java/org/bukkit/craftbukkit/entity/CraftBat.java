// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityBat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bat;

public class CraftBat extends CraftAmbient implements Bat
{
    public CraftBat(final CraftServer server, final EntityBat entity) {
        super(server, entity);
    }
    
    @Override
    public EntityBat getHandle() {
        return (EntityBat)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftBat";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.BAT;
    }
    
    @Override
    public boolean isAwake() {
        return !this.getHandle().getIsBatHanging();
    }
    
    @Override
    public void setAwake(final boolean state) {
        this.getHandle().setIsBatHanging(!state);
    }
}
