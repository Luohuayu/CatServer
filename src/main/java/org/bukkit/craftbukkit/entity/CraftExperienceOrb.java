// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb
{
    public CraftExperienceOrb(final CraftServer server, final EntityXPOrb entity) {
        super(server, entity);
    }
    
    @Override
    public int getExperience() {
        return this.getHandle().xpValue;
    }
    
    @Override
    public void setExperience(final int value) {
        this.getHandle().xpValue = value;
    }
    
    @Override
    public EntityXPOrb getHandle() {
        return (EntityXPOrb)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}
