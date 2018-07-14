// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreeperPowerEvent;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityCreeper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;

public class CraftCreeper extends CraftMonster implements Creeper
{
    public CraftCreeper(final CraftServer server, final EntityCreeper entity) {
        super(server, entity);
    }
    
    @Override
    public boolean isPowered() {
        return this.getHandle().getPowered();
    }
    
    @Override
    public void setPowered(final boolean powered) {
        final CraftServer server = this.server;
        final Creeper entity = (Creeper)this.getHandle().getBukkitEntity();
        if (powered) {
            final CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(true);
            }
        }
        else {
            final CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                this.getHandle().setPowered(false);
            }
        }
    }
    
    @Override
    public EntityCreeper getHandle() {
        return (EntityCreeper)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftCreeper";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
