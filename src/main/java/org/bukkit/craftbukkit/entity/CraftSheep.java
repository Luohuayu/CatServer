// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep
{
    public CraftSheep(final CraftServer server, final EntitySheep entity) {
        super(server, entity);
    }
    
    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte)this.getHandle().getFleeceColor().getMetadata());
    }
    
    @Override
    public void setColor(final DyeColor color) {
        this.getHandle().setFleeceColor(EnumDyeColor.byMetadata(color.getWoolData()));
    }
    
    @Override
    public boolean isSheared() {
        return this.getHandle().getSheared();
    }
    
    @Override
    public void setSheared(final boolean flag) {
        this.getHandle().setSheared(flag);
    }
    
    @Override
    public EntitySheep getHandle() {
        return (EntitySheep)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSheep";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SHEEP;
    }
}
