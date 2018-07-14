// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityLivingBase;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity
{
    public CraftComplexLivingEntity(final CraftServer server, final EntityLivingBase entity) {
        super(server, entity);
    }
    
    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
