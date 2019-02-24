package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {
    public CraftInventoryBrewer(IInventory inventory) {
        super(inventory);
    }

    public ItemStack getIngredient() {
        return getItem(3);
    }

    public void setIngredient(ItemStack ingredient) {
        setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        InventoryHolder owner = inventory.getOwner();
        return owner instanceof BrewingStand ? (BrewingStand) owner : null;
    }

    @Override
    public ItemStack getFuel() {
        return getItem(4);
    }

    @Override
    public void setFuel(ItemStack fuel) {
        setItem(4, fuel);
    }
}
