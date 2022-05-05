package catserver.server.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
    protected final CraftWorld world;
    protected final IInventory inventory;

    public CraftCustomContainer(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.inventory = (IInventory)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftCustomContainer(Block block, IInventory inventory) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.inventory);
    }

    public static class QuarkMatrixEnchantingTable extends CraftCustomContainer implements EnchantingTable {
        private static final boolean isEnable;
        private static Class<? extends TileEntity> quarkTileMatrixEnchanterBaseClass;
        private static java.lang.reflect.Method setCustomNameMethod;

        static {
            try {
                quarkTileMatrixEnchanterBaseClass = Class.forName("vazkii.quark.oddities.tile.TileMatrixEnchanterBase").asSubclass(TileEntity.class);
                try {
                    setCustomNameMethod = quarkTileMatrixEnchanterBaseClass.getDeclaredMethod("func_145920_a", String.class);
                } catch (NoSuchMethodException e) {
                    setCustomNameMethod = quarkTileMatrixEnchanterBaseClass.getDeclaredMethod("setCustomName", String.class);
                }
                setCustomNameMethod.setAccessible(true);
            } catch (Exception ignored) {}
            isEnable = (quarkTileMatrixEnchanterBaseClass != null && setCustomNameMethod != null);
        }

        public static boolean isEnable() {
            return isEnable;
        }

        public static boolean isTileMatrixEnchanterBase(TileEntity tileEntity) {
            return quarkTileMatrixEnchanterBaseClass.isInstance(tileEntity);
        }

        public QuarkMatrixEnchantingTable(Block block) {
            super(block);
            quarkTileMatrixEnchanterBaseClass.cast(this.inventory); // Check cast tile entity to get error quickly
        }

        @Override
        public String getCustomName() {
            return this.inventory.hasCustomName() ? this.inventory.getName() : null;
        }

        @Override
        public Inventory getInventory() {
            return new CraftInventoryEnchanting(this.inventory);
        }

        @Override
        public void setCustomName(String name) {
            try {
                setCustomNameMethod.invoke(this.inventory, name);
            } catch (Exception ignored) {}
        }
    }
}
