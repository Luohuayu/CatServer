package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.CartographyInventory;

public class CraftInventoryCartography extends CraftResultInventory implements CartographyInventory {

    public CraftInventoryCartography(IInventory inventory, IInventory resultInventory) {
        super(inventory, resultInventory);
    }
}
