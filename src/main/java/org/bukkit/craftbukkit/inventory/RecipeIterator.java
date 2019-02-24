package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import org.bukkit.inventory.Recipe;

import catserver.server.inventory.CustomModRecipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<IRecipe> recipes;
    private final Iterator<net.minecraft.item.ItemStack> smeltingCustom;
    private final Iterator<net.minecraft.item.ItemStack> smeltingVanilla;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.recipes = CraftingManager.REGISTRY.iterator();
        this.smeltingCustom = FurnaceRecipes.instance().customRecipes.keySet().iterator();
        this.smeltingVanilla = FurnaceRecipes.instance().smeltingList.keySet().iterator();
    }

    public boolean hasNext() {
        return recipes.hasNext() || smeltingCustom.hasNext() || smeltingVanilla.hasNext();
    }

    public Recipe next() {
        if (recipes.hasNext()) {
            removeFrom = recipes;
            // CatServer - handle custom recipe classes without Bukkit API equivalents
            IRecipe recipe = recipes.next();
            try {
                return recipe.toBukkitRecipe();
            } catch (AbstractMethodError ex) {
                // No Bukkit wrapper provided
                return recipe == null ? null :new CustomModRecipe(recipe, recipe.getRegistryName());
            }
        } else {
            net.minecraft.item.ItemStack item;
            if (smeltingCustom.hasNext()) {
                removeFrom = smeltingCustom;
                item = smeltingCustom.next();
            } else {
                removeFrom = smeltingVanilla;
                item = smeltingVanilla.next();
            }

            CraftItemStack stack = CraftItemStack.asCraftMirror(FurnaceRecipes.instance().getSmeltingResult(item));

            return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
        }
    }

    public void remove() {
        if (removeFrom == null) {
            throw new IllegalStateException();
        }
        removeFrom.remove();
    }
}
