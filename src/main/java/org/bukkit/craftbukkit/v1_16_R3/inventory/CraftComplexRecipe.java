package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.server.MinecraftServer;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftComplexRecipe implements CraftRecipe, ComplexRecipe {

    private final SpecialRecipe recipe;

    public CraftComplexRecipe(SpecialRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(recipe.getResultItem());
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(recipe.getId());
    }

    @Override
    public void addToCraftingManager() {
        MinecraftServer.getServer().getRecipeManager().addRecipe(recipe);
    }
}
