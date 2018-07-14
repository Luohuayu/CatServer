// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import net.minecraft.entity.item.EntityItem;
import org.bukkit.entity.Item;

public class CraftItem extends CraftEntity implements Item
{
    private final EntityItem item;
    
    public CraftItem(final CraftServer server, final net.minecraft.entity.Entity entity, final EntityItem item) {
        super(server, entity);
        this.item = item;
    }
    
    public CraftItem(final CraftServer server, final EntityItem entity) {
        this(server, entity, entity);
    }
    
    @Override
    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(this.item.getEntityItem());
    }
    
    @Override
    public void setItemStack(final ItemStack stack) {
        this.item.setEntityItemStack(CraftItemStack.asNMSCopy(stack));
    }
    
    @Override
    public int getPickupDelay() {
        return this.item.delayBeforeCanPickup;
    }
    
    @Override
    public void setPickupDelay(final int delay) {
        this.item.delayBeforeCanPickup = Math.min(delay, 32767);
    }
    
    @Override
    public String toString() {
        return "CraftItem";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
