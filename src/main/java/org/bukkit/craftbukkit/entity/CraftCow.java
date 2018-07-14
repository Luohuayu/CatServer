// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cow;

public class CraftCow extends CraftAnimals implements Cow
{
    public CraftCow(final CraftServer server, final EntityCow entity) {
        super(server, entity);
    }
    
    @Override
    public EntityCow getHandle() {
        return (EntityCow)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftCow";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.COW;
    }
}
