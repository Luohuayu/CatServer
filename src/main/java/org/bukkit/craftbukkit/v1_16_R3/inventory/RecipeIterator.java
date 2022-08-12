package org.bukkit.craftbukkit.v1_16_R3.inventory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Entry<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>> recipes;
    private Iterator<IRecipe<?>> current;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return (current != null && current.hasNext()) || recipes.hasNext();
    }

    @Override
    public Recipe next() {
        if (current == null || !current.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return next();
        }
        IRecipe<?> recipe = current.next();
        try {
            return recipe.toBukkitRecipe();
        } catch (Throwable e) {
            throw new RuntimeException("Error converting recipe " + recipe.getId(), e);
        }
    }

    @Override
    public void remove() {
        if (current == null) {
            throw new IllegalStateException("next() not yet called");
        }

        current.remove();
    }
}
