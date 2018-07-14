// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityGuardian;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian
{
    public CraftGuardian(final CraftServer server, final EntityGuardian entity) {
        super(server, entity);
    }
    
    @Override
    public String toString() {
        return "CraftGuardian";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }
    
    @Override
    public boolean isElder() {
        return ((EntityGuardian)this.entity).isElder();
    }
    
    @Override
    public void setElder(final boolean shouldBeElder) {
        final EntityGuardian entityGuardian = (EntityGuardian)this.entity;
        if (!this.isElder() && shouldBeElder) {
            entityGuardian.setElder(true);
        }
        else if (this.isElder() && !shouldBeElder) {
            entityGuardian.setElder(false);
            this.entity.setSize(0.85f, 0.85f);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
            entityGuardian.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
            entityGuardian.wander.setExecutionChance(80);
            entityGuardian.applyEntityAttributes();
        }
    }
}
