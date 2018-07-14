// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockDispenser;
import org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource;
import org.bukkit.projectiles.BlockProjectileSource;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityDispenser;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Dispenser;

public class CraftDispenser extends CraftBlockState implements Dispenser
{
    private final CraftWorld world;
    private final TileEntityDispenser dispenser;
    
    public CraftDispenser(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.dispenser = (TileEntityDispenser)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftDispenser(final Material material, final TileEntityDispenser te) {
        super(material);
        this.world = null;
        this.dispenser = te;
    }
    
    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.dispenser);
    }
    
    @Override
    public BlockProjectileSource getBlockProjectileSource() {
        final Block block = this.getBlock();
        if (block.getType() != Material.DISPENSER) {
            return null;
        }
        return new CraftBlockProjectileSource(this.dispenser);
    }
    
    @Override
    public boolean dispense() {
        final Block block = this.getBlock();
        if (block.getType() == Material.DISPENSER) {
            final BlockDispenser dispense = (BlockDispenser)Blocks.DISPENSER;
            dispense.dispense(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.dispenser.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityDispenser getTileEntity() {
        return this.dispenser;
    }
}
