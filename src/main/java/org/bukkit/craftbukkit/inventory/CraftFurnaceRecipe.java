// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import net.minecraft.item.crafting.FurnaceRecipes;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.FurnaceRecipe;

public class CraftFurnaceRecipe extends FurnaceRecipe implements CraftRecipe
{
    public CraftFurnaceRecipe(final ItemStack result, final ItemStack source) {
        super(result, source.getType(), source.getDurability());
    }
    
    public static CraftFurnaceRecipe fromBukkitRecipe(final FurnaceRecipe recipe) {
        if (recipe instanceof CraftFurnaceRecipe) {
            return (CraftFurnaceRecipe)recipe;
        }
        return new CraftFurnaceRecipe(recipe.getResult(), recipe.getInput());
    }
    
    @Override
    public void addToCraftingManager() {
        final ItemStack result = this.getResult();
        final ItemStack input = this.getInput();
        FurnaceRecipes.instance().registerRecipe(CraftItemStack.asNMSCopy(input), CraftItemStack.asNMSCopy(result), this.getExperience());
    }
}
