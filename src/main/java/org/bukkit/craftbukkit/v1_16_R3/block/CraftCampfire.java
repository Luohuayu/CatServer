package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.CampfireTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class CraftCampfire extends CraftBlockEntityState<CampfireTileEntity> implements Campfire {

    public CraftCampfire(Block block) {
        super(block, CampfireTileEntity.class);
    }

    public CraftCampfire(Material material, CampfireTileEntity te) {
        super(material, te);
    }

    @Override
    public int getSize() {
        return getSnapshot().getItems().size();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.item.ItemStack item = getSnapshot().getItems().get(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        getSnapshot().getItems().set(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public int getCookTime(int index) {
        return getSnapshot().cookingProgress[index];
    }

    @Override
    public void setCookTime(int index, int cookTime) {
        getSnapshot().cookingProgress[index] = cookTime;
    }

    @Override
    public int getCookTimeTotal(int index) {
        return getSnapshot().cookingTime[index];
    }

    @Override
    public void setCookTimeTotal(int index, int cookTimeTotal) {
        getSnapshot().cookingTime[index] = cookTimeTotal;
    }
}
