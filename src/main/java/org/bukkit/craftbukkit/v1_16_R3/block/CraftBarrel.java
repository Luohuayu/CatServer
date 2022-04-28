package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.util.SoundEvents;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftBarrel extends CraftLootable<BarrelTileEntity> implements Barrel {

    public CraftBarrel(Block block) {
        super(block, BarrelTileEntity.class);
    }

    public CraftBarrel(Material material, BarrelTileEntity te) {
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
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            BlockState blockData = getTileEntity().getBlockState();
            boolean open = blockData.getValue(BarrelBlock.OPEN);

            if (!open) {
                getTileEntity().updateBlockState(blockData, true);
                getTileEntity().playSound(blockData, SoundEvents.BARREL_OPEN);
            }
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            BlockState blockData = getTileEntity().getBlockState();
            getTileEntity().updateBlockState(blockData, false);
            getTileEntity().playSound(blockData, SoundEvents.BARREL_CLOSE);
        }
        getTileEntity().opened = false;
    }
}
