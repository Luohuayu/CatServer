// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand
{
    public CraftFallingSand(final CraftServer server, final EntityFallingBlock entity) {
        super(server, entity);
    }
    
    @Override
    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftFallingSand";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }
    
    @Override
    public Material getMaterial() {
        return Material.getMaterial(this.getBlockId());
    }
    
    @Override
    public int getBlockId() {
        return CraftMagicNumbers.getId(this.getHandle().getBlock().getBlock());
    }
    
    @Override
    public byte getBlockData() {
        return (byte)this.getHandle().getBlock().getBlock().getMetaFromState(this.getHandle().getBlock());
    }
    
    @Override
    public boolean getDropItem() {
        return this.getHandle().shouldDropItem;
    }
    
    @Override
    public void setDropItem(final boolean drop) {
        this.getHandle().shouldDropItem = drop;
    }
    
    @Override
    public boolean canHurtEntities() {
        return this.getHandle().hurtEntities;
    }
    
    @Override
    public void setHurtEntities(final boolean hurtEntities) {
        this.getHandle().hurtEntities = hurtEntities;
    }
    
    @Override
    public void setTicksLived(final int value) {
        super.setTicksLived(value);
        this.getHandle().fallTime = value;
    }
}
