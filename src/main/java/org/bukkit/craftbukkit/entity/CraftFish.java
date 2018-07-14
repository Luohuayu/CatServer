// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.LivingEntity;
import org.apache.commons.lang.Validate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.projectiles.ProjectileSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;

public class CraftFish extends AbstractProjectile implements Fish
{
    private double biteChance;
    
    public CraftFish(final CraftServer server, final EntityFishHook entity) {
        super(server, entity);
        this.biteChance = -1.0;
    }
    
    @Override
    public ProjectileSource getShooter() {
        if (this.getHandle().angler != null) {
            return this.getHandle().angler.getBukkitEntity();
        }
        return null;
    }
    
    @Override
    public void setShooter(final ProjectileSource shooter) {
        if (shooter instanceof CraftHumanEntity) {
            this.getHandle().angler = (EntityPlayer)((CraftHumanEntity)shooter).entity;
        }
    }
    
    @Override
    public EntityFishHook getHandle() {
        return (EntityFishHook)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftFish";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }
    
    @Override
    public double getBiteChance() {
        final EntityFishHook hook = this.getHandle();
        if (this.biteChance != -1.0) {
            return this.biteChance;
        }
        if (hook.worldObj.isRainingAt(new BlockPos(MathHelper.floor_double(hook.posX), MathHelper.floor_double(hook.posY) + 1, MathHelper.floor_double(hook.posZ)))) {
            return 0.0033333333333333335;
        }
        return 0.002;
    }
    
    @Override
    public void setBiteChance(final double chance) {
        Validate.isTrue(chance >= 0.0 && chance <= 1.0, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }
    
    @Deprecated
    @Override
    public LivingEntity _INVALID_getShooter() {
        return (LivingEntity)this.getShooter();
    }
    
    @Deprecated
    @Override
    public void _INVALID_setShooter(final LivingEntity shooter) {
        this.setShooter(shooter);
    }
}
