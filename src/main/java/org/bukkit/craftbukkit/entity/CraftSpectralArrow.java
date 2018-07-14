// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SpectralArrow;

public class CraftSpectralArrow extends CraftArrow implements SpectralArrow
{
    public CraftSpectralArrow(final CraftServer server, final EntitySpectralArrow entity) {
        super(server, entity);
    }
    
    @Override
    public EntitySpectralArrow getHandle() {
        return (EntitySpectralArrow)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSpectralArrow";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SPECTRAL_ARROW;
    }
    
    @Override
    public int getGlowingTicks() {
        return this.getHandle().duration;
    }
    
    @Override
    public void setGlowingTicks(final int duration) {
        this.getHandle().duration = duration;
    }
}
