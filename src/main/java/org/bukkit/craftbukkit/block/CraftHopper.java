// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityHopper;
import org.bukkit.block.Hopper;

public class CraftHopper extends CraftBlockState implements Hopper
{
    private final TileEntityHopper hopper;
    
    public CraftHopper(final Block block) {
        super(block);
        this.hopper = (TileEntityHopper)((CraftWorld)block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftHopper(final Material material, final TileEntityHopper te) {
        super(material);
        this.hopper = te;
    }
    
    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.hopper);
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.hopper.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityHopper getTileEntity() {
        return this.hopper;
    }
}
