// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;

public abstract class AbstractProjectile extends CraftEntity implements Projectile
{
    private boolean doesBounce;
    
    public AbstractProjectile(final CraftServer server, final net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.doesBounce = false;
    }
    
    @Override
    public boolean doesBounce() {
        return this.doesBounce;
    }
    
    @Override
    public void setBounce(final boolean doesBounce) {
        this.doesBounce = doesBounce;
    }
}
