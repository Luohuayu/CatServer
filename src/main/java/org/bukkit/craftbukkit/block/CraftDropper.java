// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockDropper;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityDropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Dropper;

public class CraftDropper extends CraftBlockState implements Dropper
{
    private final CraftWorld world;
    private final TileEntityDropper dropper;
    
    public CraftDropper(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.dropper = (TileEntityDropper)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftDropper(final Material material, final TileEntityDropper te) {
        super(material);
        this.world = null;
        this.dropper = te;
    }
    
    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.dropper);
    }
    
    @Override
    public void drop() {
        final Block block = this.getBlock();
        if (block.getType() == Material.DROPPER) {
            final BlockDropper drop = (BlockDropper)Blocks.DROPPER;
            drop.dispense(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()));
        }
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.dropper.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityDropper getTileEntity() {
        return this.dropper;
    }
}
