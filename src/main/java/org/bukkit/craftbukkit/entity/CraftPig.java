// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Pig;

public class CraftPig extends CraftAnimals implements Pig
{
    public CraftPig(final CraftServer server, final EntityPig entity) {
        super(server, entity);
    }
    
    @Override
    public boolean hasSaddle() {
        return this.getHandle().getSaddled();
    }
    
    @Override
    public void setSaddle(final boolean saddled) {
        this.getHandle().setSaddled(saddled);
    }
    
    @Override
    public EntityPig getHandle() {
        return (EntityPig)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftPig";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.PIG;
    }
}
