package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftShapedRecipe extends ShapedRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private ShapedRecipes recipe;

    public CraftShapedRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapedRecipe(ItemStack result, ShapedRecipes recipe) {
        this(recipe.key != null ? CraftNamespacedKey.fromMinecraft(recipe.key) : NamespacedKey.randomKey(), result); // CatServer - if key is null, use random key
        this.recipe = recipe;
    }

    public static CraftShapedRecipe fromBukkitRecipe(ShapedRecipe recipe) {
        if (recipe instanceof CraftShapedRecipe) {
            return (CraftShapedRecipe) recipe;
        }
        CraftShapedRecipe ret = new CraftShapedRecipe(recipe.getKey(), recipe.getResult());
        String[] shape = recipe.getShape();
        ret.shape(shape);
        Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        for (char c : ingredientMap.keySet()) {
            ItemStack stack = ingredientMap.get(c);
            if (stack != null) {
                ret.setIngredient(c, stack.getType(), stack.getDurability());
            }
        }
        return ret;
    }

    public void addToCraftingManager() {
        String[] shape = this.getShape();
        Map<Character, ItemStack> ingred = this.getIngredientMap();
        int width = shape[0].length();
        NonNullList<Ingredient> data = NonNullList.withSize(shape.length * width, Ingredient.EMPTY);

        for (int i = 0; i < shape.length; i++) {
            String row = shape[i];
            for (int j = 0; j < row.length(); j++) {
                data.set(i * width + j, Ingredient.fromStacks(new net.minecraft.item.ItemStack[]{CraftItemStack.asNMSCopy(ingred.get(row.charAt(j)))}));
            }
        }
        // TODO: Check if it's correct way to register recipes
        // CatServer - register to Forge
        ShapedRecipes recipe = new ShapedRecipes("", width, shape.length, data, CraftItemStack.asNMSCopy(this.getResult()));
        recipe.setKey(CraftNamespacedKey.toMinecraft(this.getKey()));
        recipe.setRegistryName(recipe.key);
        ((ForgeRegistry <IRecipe>) ForgeRegistries.RECIPES).unfreeze();
        ForgeRegistries.RECIPES.register(recipe);
        ((ForgeRegistry <IRecipe>) ForgeRegistries.RECIPES).freeze();
    }
}
