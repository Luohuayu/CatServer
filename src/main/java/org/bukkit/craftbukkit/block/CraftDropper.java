package org.bukkit.craftbukkit.block;

import net.minecraft.block.BlockDropper;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftLootable<TileEntityDropper> implements Dropper {

    public CraftDropper(final Block block) {
        super(block, TileEntityDropper.class);
    }

    public CraftDropper(final Material material, TileEntityDropper te) {
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
    public void drop() {
        Block block = getBlock();

        if (block.getType() == Material.DROPPER) {
            CraftWorld world = (CraftWorld) this.getWorld();
            BlockDropper drop = (BlockDropper) Blocks.DROPPER;

            drop.dispense(world.getHandle(), new BlockPos(getX(), getY(), getZ()));
        }
    }
}
