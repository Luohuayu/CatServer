// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.item.EnumDyeColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wolf;

public class CraftWolf extends CraftTameableAnimal implements Wolf
{
    public CraftWolf(final CraftServer server, final EntityWolf wolf) {
        super(server, wolf);
    }
    
    @Override
    public boolean isAngry() {
        return this.getHandle().isAngry();
    }
    
    @Override
    public void setAngry(final boolean angry) {
        this.getHandle().setAngry(angry);
    }
    
    @Override
    public EntityWolf getHandle() {
        return (EntityWolf)this.entity;
    }
    
    @Override
    public EntityType getType() {
        return EntityType.WOLF;
    }
    
    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte)this.getHandle().getCollarColor().getMetadata());
    }
    
    @Override
    public void setCollarColor(final DyeColor color) {
        this.getHandle().setCollarColor(EnumDyeColor.byMetadata(color.getWoolData()));
    }
}
