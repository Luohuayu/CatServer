// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderCrystal;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal
{
    public CraftEnderCrystal(final CraftServer server, final EntityEnderCrystal entity) {
        super(server, entity);
    }
    
    @Override
    public boolean isShowingBottom() {
        return this.getHandle().shouldShowBottom();
    }
    
    @Override
    public void setShowingBottom(final boolean showing) {
        this.getHandle().setShowBottom(showing);
    }
    
    @Override
    public Location getBeamTarget() {
        final BlockPos pos = this.getHandle().getBeamTarget();
        return (pos == null) ? null : new Location(this.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public void setBeamTarget(final Location location) {
        if (location == null) {
            this.getHandle().setBeamTarget(null);
        }
        else {
            if (location.getWorld() != this.getWorld()) {
                throw new IllegalArgumentException("Cannot set beam target location to different world");
            }
            this.getHandle().setBeamTarget(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }
    
    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
