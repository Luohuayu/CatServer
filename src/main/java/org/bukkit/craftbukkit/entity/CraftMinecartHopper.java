// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.inventory.Inventory;
import org.bukkit.entity.EntityType;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartHopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.minecart.HopperMinecart;

final class CraftMinecartHopper extends CraftMinecart implements HopperMinecart
{
    private final CraftInventory inventory;
    
    CraftMinecartHopper(final CraftServer server, final EntityMinecartHopper entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }
    
    @Override
    public String toString() {
        return "CraftMinecartHopper{inventory=" + this.inventory + '}';
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }
    
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public boolean isEnabled() {
        return ((EntityMinecartHopper)this.getHandle()).getBlocked();
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        ((EntityMinecartHopper)this.getHandle()).setBlocked(enabled);
    }
}
