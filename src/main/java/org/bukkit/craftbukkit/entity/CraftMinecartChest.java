// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.StorageMinecart;

public class CraftMinecartChest extends CraftMinecart implements StorageMinecart
{
    private final CraftInventory inventory;
    
    public CraftMinecartChest(final CraftServer server, final EntityMinecartChest entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }
    
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
    
    @Override
    public String toString() {
        return "CraftMinecartChest{inventory=" + this.inventory + '}';
    }
    
    @Override
    public EntityType getType() {
        return EntityType.MINECART_CHEST;
    }
}
