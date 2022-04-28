package org.bukkit.craftbukkit.v1_16_R3.inventory;

import java.util.List;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe {
    // TODO: Could eventually use this to add a matches() method or some such
    private net.minecraft.item.crafting.ShapelessRecipe recipe;

    public CraftShapelessRecipe(NamespacedKey key, ItemStack result) {
        super(key, result);
    }

    public CraftShapelessRecipe(ItemStack result, net.minecraft.item.crafting.ShapelessRecipe recipe) {
        this(CraftNamespacedKey.fromMinecraft(recipe.getId()), result);
        this.recipe = recipe;
    }

    public static CraftShapelessRecipe fromBukkitRecipe(ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe) recipe;
        }
        CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getKey(), recipe.getResult());
        ret.setGroup(recipe.getGroup());
        for (RecipeChoice ingred : recipe.getChoiceList()) {
            ret.addIngredient(ingred);
        }
        return ret;
    }

    @Override
    public void addToCraftingManager() {
        List<RecipeChoice> ingred = this.getChoiceList();
        NonNullList<Ingredient> data = NonNullList.withSize(ingred.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingred.size(); i++) {
            data.set(i, toNMS(ingred.get(i), true));
        }

        MinecraftServer.getServer().getRecipeManager().addRecipe(new net.minecraft.item.crafting.ShapelessRecipe(CraftNamespacedKey.toMinecraft(this.getKey()), this.getGroup(), CraftItemStack.asNMSCopy(this.getResult()), data));
    }
}
