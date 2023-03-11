package org.bukkit.craftbukkit.v1_18_R2.inventory;

import org.bukkit.inventory.LoomInventory;

public class CraftInventoryLoom extends CraftResultInventory implements LoomInventory {

    public CraftInventoryLoom(net.minecraft.world.Container inventory, net.minecraft.world.Container resultInventory) {
        super(inventory, resultInventory);
    }
}
