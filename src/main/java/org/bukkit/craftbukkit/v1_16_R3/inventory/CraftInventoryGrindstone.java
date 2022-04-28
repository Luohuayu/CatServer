package org.bukkit.craftbukkit.v1_16_R3.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.GrindstoneInventory;

public class CraftInventoryGrindstone extends CraftResultInventory implements GrindstoneInventory {

    public CraftInventoryGrindstone(IInventory inventory, IInventory resultInventory) {
        super(inventory, resultInventory);
    }
}
