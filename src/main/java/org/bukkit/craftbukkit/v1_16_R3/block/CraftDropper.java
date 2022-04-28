package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.DropperBlock;
import net.minecraft.tileentity.DropperTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftLootable<DropperTileEntity> implements Dropper {

    public CraftDropper(final Block block) {
        super(block, DropperTileEntity.class);
    }

    public CraftDropper(final Material material, DropperTileEntity te) {
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
            DropperBlock drop = (DropperBlock) Blocks.DROPPER;

            drop.dispenseFrom(world.getHandle(), this.getPosition());
        }
    }
}
