package catserver.server.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CraftMatrixEnchantingTable extends CraftCustomContainer implements EnchantingTable {
    public CraftMatrixEnchantingTable(final Block block) {
        super(block);
        // Check cast tile entity to get error quickly
        ClassBase.cast(this.inventory);
    }

    @Override
    public String getCustomName() {
        return this.inventory.hasCustomName() ? this.inventory.getName() : null;
    }

    @Override
    public void setCustomName(String name) {
        try {
            ClassBase_setCustomName.invoke(this.inventory, name);
        } catch (ReflectiveOperationException ignored) {}
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventoryEnchanting(this.inventory);
    }

    // MatrixEnchantingTable utils
    private static final Class<? extends TileEntity> ClassBase;
    private static final Method ClassBase_setCustomName;

    static {
        Class<? extends TileEntity> ClassBaseTmp;
        Method ClassBaseTmp_setCustomName = null;
        try {
            ClassBaseTmp =
                    Class.forName("vazkii.quark.oddities.tile.TileMatrixEnchanterBase")
                            .asSubclass(TileEntity.class);
            try {
                ClassBaseTmp_setCustomName = ClassBaseTmp.getDeclaredMethod(
                        "setCustomName", String.class);
            } catch (NoSuchMethodException e) {
                // Try srg name as fallback if using qualified name failed
                ClassBaseTmp_setCustomName = ClassBaseTmp.getDeclaredMethod(
                        "func_145920_a", String.class);
            }
            if (!Modifier.isPublic(ClassBaseTmp_setCustomName.getModifiers()))
                ClassBaseTmp_setCustomName.setAccessible(true);
        } catch (Exception e) {
            ClassBaseTmp = null;
        }
        ClassBase = ClassBaseTmp;
        ClassBase_setCustomName =
                ClassBaseTmp_setCustomName;
    }

    public static boolean isNmsInstanceOf(Object instance) {
        return ClassBase != null && ClassBase.isInstance(instance);
    }
}
