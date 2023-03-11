package org.bukkit.craftbukkit.v1_18_R2.inventory;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventoryAbstractHorse implements HorseInventory {

    public CraftInventoryHorse(net.minecraft.world.Container inventory) {
        super(inventory);
    }

    @Override
    public ItemStack getArmor() {
       return getItem(1);
    }

    @Override
    public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
