// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftAgeable implements Animals
{
    public CraftAnimals(final CraftServer server, final EntityAnimal entity) {
        super(server, entity);
    }
    
    @Override
    public EntityAnimal getHandle() {
        return (EntityAnimal)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftAnimals";
    }
}
