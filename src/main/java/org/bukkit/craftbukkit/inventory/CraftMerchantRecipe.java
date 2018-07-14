// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import java.util.List;
import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchantRecipe extends MerchantRecipe
{
    private final net.minecraft.village.MerchantRecipe handle;
    
    public CraftMerchantRecipe(final net.minecraft.village.MerchantRecipe merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.itemToSell), 0);
        this.handle = merchantRecipe;
        this.addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.itemToBuy));
        this.addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.secondItemToBuy));
    }
    
    public CraftMerchantRecipe(final ItemStack result, final int uses, final int maxUses, final boolean experienceReward) {
        super(result, uses, maxUses, experienceReward);
        this.handle = new net.minecraft.village.MerchantRecipe(null, null, CraftItemStack.asNMSCopy(result), uses, maxUses, this);
    }
    
    @Override
    public int getUses() {
        return this.handle.toolUses;
    }
    
    @Override
    public void setUses(final int uses) {
        this.handle.toolUses = uses;
    }
    
    @Override
    public int getMaxUses() {
        return this.handle.maxTradeUses;
    }
    
    @Override
    public void setMaxUses(final int maxUses) {
        this.handle.maxTradeUses = maxUses;
    }
    
    @Override
    public boolean hasExperienceReward() {
        return this.handle.rewardsExp;
    }
    
    @Override
    public void setExperienceReward(final boolean flag) {
        this.handle.rewardsExp = flag;
    }
    
    public net.minecraft.village.MerchantRecipe toMinecraft() {
        final List<ItemStack> ingredients = this.getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), (Object)"No offered ingredients");
        this.handle.itemToBuy = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            this.handle.secondItemToBuy = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return this.handle;
    }
    
    public static CraftMerchantRecipe fromBukkit(final MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe)recipe;
        }
        final CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward());
        craft.setIngredients(recipe.getIngredients());
        return craft;
    }
}
