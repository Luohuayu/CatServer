package org.bukkit.craftbukkit.v1_20_R1.inventory;

import net.minecraft.world.Container;
import org.bukkit.inventory.GrindstoneInventory;

public class CraftInventoryGrindstone extends CraftResultInventory implements GrindstoneInventory {

    public CraftInventoryGrindstone(Container inventory, Container resultInventory) {
        super(inventory, resultInventory);
    }
}
