// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import org.bukkit.inventory.MerchantRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import org.bukkit.inventory.MerchantInventory;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory
{
    public CraftInventoryMerchant(final InventoryMerchant merchant) {
        super(merchant);
    }
    
    @Override
    public int getSelectedRecipeIndex() {
        return this.getInventory().currentRecipeIndex;
    }
    
    @Override
    public MerchantRecipe getSelectedRecipe() {
        return this.getInventory().getCurrentRecipe().asBukkit();
    }
    
    @Override
    public InventoryMerchant getInventory() {
        return (InventoryMerchant)this.inventory;
    }
}
