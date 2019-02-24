package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;

public class CraftEnchantingTable extends CraftBlockEntityState<TileEntityEnchantmentTable> implements EnchantingTable {

    public CraftEnchantingTable(final Block block) {
        super(block, TileEntityEnchantmentTable.class);
    }

    public CraftEnchantingTable(final Material material, final TileEntityEnchantmentTable te) {
        super(material, te);
    }

    @Override
    public String getCustomName() {
        TileEntityEnchantmentTable enchant = this.getSnapshot();
        return enchant.hasCustomName() ? enchant.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(name);
    }

    @Override
    public void applyTo(TileEntityEnchantmentTable enchantingTable) {
        super.applyTo(enchantingTable);

        if (!this.getSnapshot().hasCustomName()) {
            enchantingTable.setCustomName(null);
        }
    }
}
