package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryFurnace extends CraftInventory implements FurnaceInventory {
    public CraftInventoryFurnace(AbstractFurnaceTileEntity inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getResult() {
        return getItem(2);
    }

    @Override
    public ItemStack getFuel() {
        return getItem(1);
    }

    @Override
    public ItemStack getSmelting() {
        return getItem(0);
    }

    @Override
    public void setFuel(ItemStack stack) {
        setItem(1, stack);
    }

    @Override
    public void setResult(ItemStack stack) {
        setItem(2, stack);
    }

    @Override
    public void setSmelting(ItemStack stack) {
        setItem(0, stack);
    }

    @Override
    public Furnace getHolder() {
        // LoliServer start - From CatServer
        org.bukkit.inventory.InventoryHolder owner = inventory.getOwner();
        return owner instanceof Furnace ? (Furnace) owner : null;
        // LoliServer end
    }
}
