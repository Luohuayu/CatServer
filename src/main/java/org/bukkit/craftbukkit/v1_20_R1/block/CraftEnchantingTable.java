package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import org.bukkit.World;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage;

public class CraftEnchantingTable extends CraftBlockEntityState<EnchantmentTableBlockEntity> implements EnchantingTable {

    public CraftEnchantingTable(World world, EnchantmentTableBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public String getCustomName() {
        EnchantmentTableBlockEntity enchant = this.getSnapshot();
        return enchant.hasCustomName() ? CraftChatMessage.fromComponent(enchant.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(EnchantmentTableBlockEntity enchantingTable) {
        super.applyTo(enchantingTable);

        if (!this.getSnapshot().hasCustomName()) {
            enchantingTable.setCustomName(null);
        }
    }
}
