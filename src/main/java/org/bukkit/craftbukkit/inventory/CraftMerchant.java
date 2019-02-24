package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipeList;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchant implements Merchant {

    protected final IMerchant merchant;

    public CraftMerchant(IMerchant merchant) {
        this.merchant = merchant;
    }

    public IMerchant getMerchant() {
        return merchant;
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(merchant.getRecipes(null), new com.google.common.base.Function<net.minecraft.village.MerchantRecipe, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.village.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        MerchantRecipeList recipesList = merchant.getRecipes(null);
        recipesList.clear();
        for (MerchantRecipe recipe : recipes) {
            recipesList.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft());
        }
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return merchant.getRecipes(null).get(i).asBukkit();
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        merchant.getRecipes(null).set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    public int getRecipeCount() {
        return merchant.getRecipes(null).size();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        EntityPlayer eh = merchant.getCustomer();
        return eh == null ? null : eh.getBukkitEntity();
    }

    @Override
    public int hashCode() {
        return merchant.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftMerchant && ((CraftMerchant) obj).merchant.equals(this.merchant);
    }
}
