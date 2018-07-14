// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityPolarBear;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PolarBear;

public class CraftPolarBear extends CraftAnimals implements PolarBear
{
    public CraftPolarBear(final CraftServer server, final EntityPolarBear entity) {
        super(server, entity);
    }
    
    @Override
    public EntityPolarBear getHandle() {
        return (EntityPolarBear)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftPolarBear";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.POLAR_BEAR;
    }
}
