// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.entity.LivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityCreature;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;

public class CraftCreature extends CraftLivingEntity implements Creature
{
    public CraftCreature(final CraftServer server, final EntityCreature entity) {
        super(server, entity);
    }
    
    @Override
    public void setTarget(final LivingEntity target) {
        final EntityCreature entity = this.getHandle();
        if (target == null) {
            entity.setGoalTarget(null, null, false);
        }
        else if (target instanceof CraftLivingEntity) {
            entity.setGoalTarget(((CraftLivingEntity)target).getHandle(), null, false);
        }
    }
    
    @Override
    public CraftLivingEntity getTarget() {
        if (this.getHandle().getAttackTarget() == null) {
            return null;
        }
        return (CraftLivingEntity)this.getHandle().getAttackTarget().getBukkitEntity();
    }
    
    @Override
    public EntityCreature getHandle() {
        return (EntityCreature)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftCreature";
    }
}
