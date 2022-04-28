package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.SoundEvents;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<ChestTileEntity> implements Chest {

    public CraftChest(final Block block) {
        super(block, ChestTileEntity.class);
    }

    public CraftChest(final Material material, final ChestTileEntity te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getBlockInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public Inventory getInventory() {
        CraftInventory inventory = (CraftInventory) this.getBlockInventory();
        if (!isPlaced()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in ChestBlock.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock blockChest = (ChestBlock) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        INamedContainerProvider nms = blockChest.getMenuProvider(data, world.getHandle(), this.getPosition());

        if (nms instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            net.minecraft.block.Block block = getTileEntity().getBlockState().getBlock();
            getTileEntity().getLevel().blockEvent(getTileEntity().getBlockPos(), block, 1, getTileEntity().openCount + 1);
            getTileEntity().playSound(SoundEvents.CHEST_OPEN);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            net.minecraft.block.Block block = getTileEntity().getBlockState().getBlock();
            getTileEntity().getLevel().blockEvent(getTileEntity().getBlockPos(), block, 1, 0);
            getTileEntity().playSound(SoundEvents.CHEST_CLOSE);
        }
        getTileEntity().opened = false;
    }
}
