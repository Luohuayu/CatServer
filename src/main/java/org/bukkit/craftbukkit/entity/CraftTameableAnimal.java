// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.entity.AnimalTamer;
import java.util.UUID;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature
{
    public CraftTameableAnimal(final CraftServer server, final EntityTameable entity) {
        super(server, entity);
    }
    
    @Override
    public EntityTameable getHandle() {
        return (EntityTameable)super.getHandle();
    }
    
    public UUID getOwnerUUID() {
        try {
            return this.getHandle().getOwnerId();
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public void setOwnerUUID(final UUID uuid) {
        this.getHandle().setOwnerId(uuid);
    }
    
    @Override
    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        AnimalTamer owner = this.getServer().getPlayer(this.getOwnerUUID());
        if (owner == null) {
            owner = this.getServer().getOfflinePlayer(this.getOwnerUUID());
        }
        return owner;
    }
    
    @Override
    public boolean isTamed() {
        return this.getHandle().isTamed();
    }
    
    @Override
    public void setOwner(final AnimalTamer tamer) {
        if (tamer != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget(null, null, false);
            this.setOwnerUUID(tamer.getUniqueId());
        }
        else {
            this.setTamed(false);
            this.setOwnerUUID(null);
        }
    }
    
    @Override
    public void setTamed(final boolean tame) {
        this.getHandle().setTamed(tame);
        if (!tame) {
            this.setOwnerUUID(null);
        }
    }
    
    public boolean isSitting() {
        return this.getHandle().isSitting();
    }
    
    public void setSitting(final boolean sitting) {
        this.getHandle().setSitting(sitting);
        this.getHandle().getAISit().setSitting(sitting);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getClass().getSimpleName()) + "{owner=" + this.getOwner() + ",tamed=" + this.isTamed() + "}";
    }
}
