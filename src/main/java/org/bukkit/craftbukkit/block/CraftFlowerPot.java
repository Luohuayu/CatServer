package org.bukkit.craftbukkit.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFlowerPot;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.material.MaterialData;

public class CraftFlowerPot extends CraftBlockEntityState<TileEntityFlowerPot> implements FlowerPot {

    private MaterialData contents;

    public CraftFlowerPot(Block block) {
        super(block, TileEntityFlowerPot.class);
        initSnapshotFromNbt(); // CatServer
    }

    public CraftFlowerPot(Material material, TileEntityFlowerPot te) {
        super(material, te);
        initSnapshotFromNbt(); // CatServer
    }

    @Override
    public void load(TileEntityFlowerPot pot) {
        super.load(pot);

        contents = (pot.getFlowerPotItem() == null) ? null : CraftItemStack.asBukkitCopy(pot.getFlowerItemStack()).getData();
    }

    @Override
    public MaterialData getContents() {
        return contents;
    }

    @Override
    public void setContents(MaterialData item) {
        contents = item;
    }

    @Override
    public void applyTo(TileEntityFlowerPot pot) {
        super.applyTo(pot);

        pot.setItemStack(contents == null ? ItemStack.EMPTY : CraftItemStack.asNMSCopy(contents.toItemStack(1)));
    }
}
