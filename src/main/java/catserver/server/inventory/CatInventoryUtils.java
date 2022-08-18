package catserver.server.inventory;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CatInventoryUtils {
    public static void onOpen(Container inventory, CraftHumanEntity who) {
        inventory.onOpen(who);
    }

    public static void onClose(Container inventory, CraftHumanEntity who) {
        inventory.onClose(who);
    }

    public static List<HumanEntity> getViewers(Container inventory) {
        return inventory.getViewers();
    }

    public static java.util.List<net.minecraft.world.item.ItemStack> getContents(Container inventory) {
        return inventory.getContents();
    }

    public static InventoryHolder getOwner(Container inventory) {
        return inventory.getOwner();
    }

    public static void setMaxStackSize(Container inventory, int size) {
        inventory.setMaxStackSize(size);
    }

    public static Location getLocation(Container inventory) {
        return inventory.getLocation();
    }

    public static InventoryHolder getOwner(BlockEntity tileEntity) {
        return getOwner(tileEntity.level, tileEntity.getBlockPos());
    }

    public static InventoryHolder getOwner(Level world, BlockPos pos) {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (block == null) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "No block for owner at %s %d %d %d", new Object[]{world.getWorld(), pos.getX(), pos.getY(), pos.getZ()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState();
        if (state instanceof InventoryHolder) {
            return (InventoryHolder) state;
        } else if (state instanceof CraftBlockEntityState) { // CatServer
            BlockEntity te = ((CraftBlockEntityState) state).getTileEntity();
            if (te instanceof Container) {
                return new CatCustomInventory((Container) te);
            }
        }
        return null;
    }

    public static Inventory getBukkitInventory(Container inventory) {
        InventoryHolder owner = CatInventoryUtils.getOwner(inventory);
        return (owner == null ? new CatCustomInventory(inventory).getInventory() : owner.getInventory());
    }
}
