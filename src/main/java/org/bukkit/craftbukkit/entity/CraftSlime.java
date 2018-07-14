// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftLivingEntity implements Slime
{
    public CraftSlime(final CraftServer server, final EntitySlime entity) {
        super(server, entity);
    }
    
    @Override
    public int getSize() {
        return this.getHandle().getSlimeSize();
    }
    
    @Override
    public void setSize(final int size) {
        this.getHandle().setSlimeSize(size);
    }
    
    @Override
    public EntitySlime getHandle() {
        return (EntitySlime)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSlime";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SLIME;
    }
}
