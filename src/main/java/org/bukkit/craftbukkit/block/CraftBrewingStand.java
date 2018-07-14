// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.inventory.Inventory;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityBrewingStand;
import org.bukkit.block.BrewingStand;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand
{
    private final TileEntityBrewingStand brewingStand;
    
    public CraftBrewingStand(final Block block) {
        super(block);
        this.brewingStand = (TileEntityBrewingStand)((CraftWorld)block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftBrewingStand(final Material material, final TileEntityBrewingStand te) {
        super(material);
        this.brewingStand = te;
    }
    
    @Override
    public BrewerInventory getInventory() {
        return new CraftInventoryBrewer(this.brewingStand);
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.brewingStand.markDirty();
        }
        return result;
    }
    
    @Override
    public int getBrewingTime() {
        return this.brewingStand.getField(0);
    }
    
    @Override
    public void setBrewingTime(final int brewTime) {
        this.brewingStand.setField(0, brewTime);
    }
    
    @Override
    public TileEntityBrewingStand getTileEntity() {
        return this.brewingStand;
    }
    
    @Override
    public int getFuelLevel() {
        return this.brewingStand.getField(1);
    }
    
    @Override
    public void setFuelLevel(final int level) {
        this.brewingStand.setField(1, level);
    }
}
