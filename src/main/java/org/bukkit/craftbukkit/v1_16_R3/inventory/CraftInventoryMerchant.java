package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.entity.merchant.IMerchant;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    private final IMerchant merchant;

    public CraftInventoryMerchant(IMerchant merchant, net.minecraft.inventory.MerchantInventory inventory) {
        super(inventory);
        this.merchant = merchant;
    }

    @Override
    public int getSelectedRecipeIndex() {
        return getInventory().selectionHint;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        net.minecraft.item.MerchantOffer nmsRecipe = getInventory().getActiveOffer();
        return (nmsRecipe == null) ? null : nmsRecipe.asBukkit();
    }

    @Override
    public net.minecraft.inventory.MerchantInventory getInventory() {
        return (net.minecraft.inventory.MerchantInventory) inventory;
    }

    @Override
    public Merchant getMerchant() {
        return merchant.getCraftMerchant();
    }
}
