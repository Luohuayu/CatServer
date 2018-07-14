// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import net.minecraft.entity.boss.EntityDragon;
import org.bukkit.entity.ComplexLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragonPart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;

public class CraftComplexPart extends CraftEntity implements ComplexEntityPart
{
    public CraftComplexPart(final CraftServer server, final EntityDragonPart entity) {
        super(server, entity);
    }
    
    @Override
    public ComplexLivingEntity getParent() {
        return (ComplexLivingEntity)((EntityDragon)this.getHandle().entityDragonObj).getBukkitEntity();
    }
    
    @Override
    public void setLastDamageCause(final EntityDamageEvent cause) {
        this.getParent().setLastDamageCause(cause);
    }
    
    @Override
    public EntityDamageEvent getLastDamageCause() {
        return this.getParent().getLastDamageCause();
    }
    
    @Override
    public boolean isValid() {
        return this.getParent().isValid();
    }
    
    @Override
    public EntityDragonPart getHandle() {
        return (EntityDragonPart)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftComplexPart";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.COMPLEX_PART;
    }
}
