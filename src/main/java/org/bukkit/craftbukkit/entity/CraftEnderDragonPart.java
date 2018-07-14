// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.util.NumberConversions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EnderDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderDragonPart;

public class CraftEnderDragonPart extends CraftComplexPart implements EnderDragonPart
{
    public CraftEnderDragonPart(final CraftServer server, final EntityDragonPart entity) {
        super(server, entity);
    }
    
    @Override
    public EnderDragon getParent() {
        return (EnderDragon)super.getParent();
    }
    
    @Override
    public EntityDragonPart getHandle() {
        return (EntityDragonPart)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEnderDragonPart";
    }
    
    @Override
    public void damage(final double amount) {
        this.getParent().damage(amount);
    }
    
    @Override
    public void damage(final double amount, final Entity source) {
        this.getParent().damage(amount, source);
    }
    
    @Override
    public double getHealth() {
        return this.getParent().getHealth();
    }
    
    @Override
    public void setHealth(final double health) {
        this.getParent().setHealth(health);
    }
    
    @Override
    public double getMaxHealth() {
        return this.getParent().getMaxHealth();
    }
    
    @Override
    public void setMaxHealth(final double health) {
        this.getParent().setMaxHealth(health);
    }
    
    @Override
    public void resetMaxHealth() {
        this.getParent().resetMaxHealth();
    }
    
    @Deprecated
    @Override
    public void _INVALID_damage(final int amount) {
        this.damage(amount);
    }
    
    @Deprecated
    @Override
    public void _INVALID_damage(final int amount, final Entity source) {
        this.damage(amount, source);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getHealth() {
        return NumberConversions.ceil(this.getHealth());
    }
    
    @Deprecated
    @Override
    public void _INVALID_setHealth(final int health) {
        this.setHealth(health);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getMaxHealth() {
        return NumberConversions.ceil(this.getMaxHealth());
    }
    
    @Deprecated
    @Override
    public void _INVALID_setMaxHealth(final int health) {
        this.setMaxHealth(health);
    }
}
