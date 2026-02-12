package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.inventory.Inventory;

public class CraftChest extends CraftLootable<ChestBlockEntity> implements Chest {

    public CraftChest(World world, ChestBlockEntity tileEntity) {
        super(world, tileEntity);
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
        if (!isPlaced() || isWorldGeneration()) {
            return inventory;
        }

        // The logic here is basically identical to the logic in BlockChest.interact
        CraftWorld world = (CraftWorld) this.getWorld();

        ChestBlock blockChest = (ChestBlock) (this.getType() == Material.CHEST ? Blocks.CHEST : Blocks.TRAPPED_CHEST);
        MenuProvider nms = blockChest.getMenuProvider(data, world.getHandle(), this.getPosition(), true);

        if (nms instanceof ChestBlock.DoubleInventory) {
            inventory = new CraftInventoryDoubleChest((ChestBlock.DoubleInventory) nms);
        }
        return inventory;
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIOpen((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block, openCount, openCount + 1);
        }
        getTileEntity().openersCounter.opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().openersCounter.opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            BlockState block = getTileEntity().getBlockState();
            int openCount = getTileEntity().openersCounter.getOpenerCount();

            getTileEntity().openersCounter.onAPIClose((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block);
            getTileEntity().openersCounter.openerAPICountChanged((net.minecraft.world.level.Level) getWorldHandle(), getPosition(), block, openCount, 0);
        }
        getTileEntity().openersCounter.opened = false;
    }
}
