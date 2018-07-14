// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MushroomCow;

public class CraftMushroomCow extends CraftCow implements MushroomCow
{
    public CraftMushroomCow(final CraftServer server, final EntityMooshroom entity) {
        super(server, entity);
    }
    
    @Override
    public EntityMooshroom getHandle() {
        return (EntityMooshroom)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftMushroomCow";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MUSHROOM_COW;
    }
}
