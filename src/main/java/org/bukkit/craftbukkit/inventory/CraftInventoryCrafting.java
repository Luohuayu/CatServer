package org.bukkit.craftbukkit.inventory;

import java.util.Arrays;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import catserver.server.inventory.CustomModRecipe;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private final IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getResultInventory().getSizeInventory() + getMatrixInventory().getSizeInventory();
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }
        setContents(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        List<net.minecraft.item.ItemStack> mcResultItems = getResultInventory().getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.size(); i++ ) {
            items[i] = CraftItemStack.asCraftMirror(mcResultItems.get(i));
        }

        List<net.minecraft.item.ItemStack> mcItems = getMatrixInventory().getContents();

        for (int j = 0; j < mcItems.size(); j++) {
            items[i + j] = CraftItemStack.asCraftMirror(mcItems.get(j));
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < getResultInventory().getSizeInventory()) {
            net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(index);
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        } else {
            net.minecraft.item.ItemStack item = getMatrixInventory().getStackInSlot(index - getResultInventory().getSizeInventory());
            return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getResultInventory().getSizeInventory()) {
            getResultInventory().setInventorySlotContents(index, CraftItemStack.asNMSCopy(item));
        } else {
            getMatrixInventory().setInventorySlotContents((index - getResultInventory().getSizeInventory()), CraftItemStack.asNMSCopy(item));
        }
    }

    public ItemStack[] getMatrix() {
        List<net.minecraft.item.ItemStack> matrix = getMatrixInventory().getContents();

        return asCraftMirror(matrix);
    }

    public ItemStack getResult() {
        net.minecraft.item.ItemStack item = getResultInventory().getStackInSlot(0);
        if (!item.isEmpty()) return CraftItemStack.asCraftMirror(item);
        return null;
    }

    public void setMatrix(ItemStack[] contents) {
        if (getMatrixInventory().getSizeInventory() > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getMatrixInventory().getSizeInventory() + " or less");
        }

        for (int i = 0; i < getMatrixInventory().getSizeInventory(); i++) {
            if (i < contents.length) {
                getMatrixInventory().setInventorySlotContents(i, CraftItemStack.asNMSCopy(contents[i]));
            } else {
                getMatrixInventory().setInventorySlotContents(i, net.minecraft.item.ItemStack.EMPTY);
            }
        }
    }

    public void setResult(ItemStack item) {
        List<net.minecraft.item.ItemStack> contents = getResultInventory().getContents();
        contents.set(0, CraftItemStack.asNMSCopy(item));
    }

    public Recipe getRecipe() {
        // CatServer - handle custom recipe classes without Bukkit API equivalents
        IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
        try {
            return recipe == null ? null : recipe.toBukkitRecipe();
        } catch (AbstractMethodError ex) {
            // No Bukkit wrapper provided
            return recipe == null ? null : new CustomModRecipe(recipe, recipe.getRegistryName());
        }
    }
}
