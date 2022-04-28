package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingInventory;

public class CraftInventorySmithing extends CraftResultInventory implements SmithingInventory {

    private final Location location;

    public CraftInventorySmithing(Location location, IInventory inventory, net.minecraft.inventory.CraftResultInventory resultInventory) {
        super(inventory, resultInventory);
        this.location = location;
    }

    @Override
    public net.minecraft.inventory.CraftResultInventory getResultInventory() {
        return (net.minecraft.inventory.CraftResultInventory) super.getResultInventory();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public ItemStack getResult() {
        return getItem(2);
    }

    @Override
    public void setResult(ItemStack item) {
        setItem(2, item);
    }

    @Override
    public Recipe getRecipe() {
        IRecipe recipe = getResultInventory().getCurrentRecipe();
        return (recipe == null) ? null : recipe.toBukkitRecipe();
    }
}