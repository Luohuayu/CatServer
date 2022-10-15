package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser extends CraftLootable<TileEntityDispenser> implements Dispenser {

    public CraftDispenser(final Block block) {
        super(block, TileEntityDispenser.class);
    }

    public CraftDispenser(final Material material, final TileEntityDispenser te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public BlockProjectileSource getBlockProjectileSource() {
        Block block = getBlock();

        if (block.getType() != Material.DISPENSER) {
            return null;
        }

        return new CraftBlockProjectileSource(new BlockSourceImpl(((CraftWorld) this.getWorld()).getHandle(), new BlockPos(getX(), getY(), getZ()))); // Catserver - Fix mod block reusing BehaviorProjectileDispense causing server crash
    }

    @Override
    public boolean dispense() {
        Block block = getBlock();

        if (block.getType() == Material.DISPENSER) {
            CraftWorld world = (CraftWorld) this.getWorld();
            BlockDispenser dispense = (BlockDispenser) Blocks.DISPENSER;

            dispense.dispense(world.getHandle(), new BlockPos(getX(), getY(), getZ()));
            return true;
        } else {
            return false;
        }
    }
}
