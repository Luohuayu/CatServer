// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAmbientCreature;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ambient;

public class CraftAmbient extends CraftLivingEntity implements Ambient
{
    public CraftAmbient(final CraftServer server, final EntityAmbientCreature entity) {
        super(server, entity);
    }
    
    @Override
    public EntityAmbientCreature getHandle() {
        return (EntityAmbientCreature)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftAmbient";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
