// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import java.util.Iterator;
import org.bukkit.inventory.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftShapelessRecipe extends ShapelessRecipe implements CraftRecipe
{
    private ShapelessRecipes recipe;
    
    public CraftShapelessRecipe(final ItemStack result) {
        super(result);
    }
    
    public CraftShapelessRecipe(final ItemStack result, final ShapelessRecipes recipe) {
        this(result);
        this.recipe = recipe;
    }
    
    public static CraftShapelessRecipe fromBukkitRecipe(final ShapelessRecipe recipe) {
        if (recipe instanceof CraftShapelessRecipe) {
            return (CraftShapelessRecipe)recipe;
        }
        final CraftShapelessRecipe ret = new CraftShapelessRecipe(recipe.getResult());
        for (final ItemStack ingred : recipe.getIngredientList()) {
            ret.addIngredient(ingred.getType(), ingred.getDurability());
        }
        return ret;
    }
    
    @Override
    public void addToCraftingManager() {
        final List<ItemStack> ingred = this.getIngredientList();
        final Object[] data = new Object[ingred.size()];
        int i = 0;
        for (final ItemStack mdata : ingred) {
            final int id = mdata.getTypeId();
            final short dmg = mdata.getDurability();
            data[i] = new net.minecraft.item.ItemStack(CraftMagicNumbers.getItem(id), 1, dmg);
            ++i;
        }
        CraftingManager.getInstance().addShapelessRecipe(CraftItemStack.asNMSCopy(this.getResult()), data);
    }
}
