// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityFurnace;
import org.bukkit.block.Furnace;

public class CraftFurnace extends CraftBlockState implements Furnace
{
    private final TileEntityFurnace furnace;
    
    public CraftFurnace(final Block block) {
        super(block);
        this.furnace = (TileEntityFurnace)((CraftWorld)block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftFurnace(final Material material, final TileEntityFurnace te) {
        super(material);
        this.furnace = te;
    }
    
    @Override
    public FurnaceInventory getInventory() {
        return new CraftInventoryFurnace(this.furnace);
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.furnace.markDirty();
        }
        return result;
    }
    
    @Override
    public short getBurnTime() {
        return (short)this.furnace.getField(0);
    }
    
    @Override
    public void setBurnTime(final short burnTime) {
        this.furnace.setField(0, burnTime);
    }
    
    @Override
    public short getCookTime() {
        return (short)this.furnace.getField(2);
    }
    
    @Override
    public void setCookTime(final short cookTime) {
        this.furnace.setField(2, cookTime);
    }
    
    @Override
    public TileEntityFurnace getTileEntity() {
        return this.furnace;
    }
}
