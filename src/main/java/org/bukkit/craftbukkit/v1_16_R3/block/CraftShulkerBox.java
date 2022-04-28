package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable<ShulkerBoxTileEntity> implements ShulkerBox {

    public CraftShulkerBox(final Block block) {
        super(block, ShulkerBoxTileEntity.class);
    }

    public CraftShulkerBox(final Material material, final ShulkerBoxTileEntity te) {
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
    public DyeColor getColor() {
        net.minecraft.item.DyeColor color = ((ShulkerBoxBlock) CraftMagicNumbers.getBlock(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getColorValue());
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened) {
            World world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 1);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened) {
            World world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 0);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }
}
