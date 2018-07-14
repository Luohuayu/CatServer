// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Chicken;

public class CraftChicken extends CraftAnimals implements Chicken
{
    public CraftChicken(final CraftServer server, final EntityChicken entity) {
        super(server, entity);
    }
    
    @Override
    public EntityChicken getHandle() {
        return (EntityChicken)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftChicken";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.CHICKEN;
    }
}
