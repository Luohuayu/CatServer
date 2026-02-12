package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;

public class CraftShulkerBox extends CraftLootable<ShulkerBoxBlockEntity> implements ShulkerBox {

    public CraftShulkerBox(World world, ShulkerBoxBlockEntity tileEntity) {
        super(world, tileEntity);
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
        net.minecraft.world.item.DyeColor color = ((ShulkerBoxBlock) CraftMagicNumbers.getBlock(this.getType())).color;

        return (color == null) ? null : DyeColor.getByWoolData((byte) color.getId());
    }

    @Override
    public void open() {
        requirePlaced();
        if (!getTileEntity().opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            net.minecraft.world.level.Level world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 1);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = true;
    }

    @Override
    public void close() {
        requirePlaced();
        if (getTileEntity().opened && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            net.minecraft.world.level.Level world = getTileEntity().getLevel();
            world.blockEvent(getPosition(), getTileEntity().getBlockState().getBlock(), 1, 0);
            world.playSound(null, getPosition(), SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
        getTileEntity().opened = false;
    }
}
