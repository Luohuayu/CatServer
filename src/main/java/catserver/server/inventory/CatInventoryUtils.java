package catserver.server.inventory;

import java.util.List;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CatInventoryUtils {
    public static void onOpen(IInventory inventory, CraftHumanEntity who) {
        inventory.onOpen(who);
    }

    public static void onClose(IInventory inventory, CraftHumanEntity who) {
        inventory.onClose(who);
    }

    public static List<HumanEntity> getViewers(IInventory inventory) {
        return inventory.getViewers();
    }

    public static java.util.List<net.minecraft.item.ItemStack> getContents(IInventory inventory) {
        return inventory.getContents();
    }

    public static InventoryHolder getOwner(IInventory inventory) {
        return inventory.getOwner();
    }

    public static void setMaxStackSize(IInventory inventory, int size) {
        inventory.setMaxStackSize(size);
    }

    public static Location getLocation(IInventory inventory) {
        return inventory.getLocation();
    }

    public static InventoryHolder getOwner(TileEntity tileEntity) {
        return getOwner(tileEntity.level, tileEntity.getBlockPos());
    }

    public static InventoryHolder getOwner(World world, BlockPos pos) {
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
            TileEntity te = ((CraftBlockEntityState) state).getTileEntity();
            if (te instanceof IInventory) {
                return new CatCustomInventory((IInventory) te);
            }
        }
        return null;
    }

    public static Inventory getBukkitInventory(IInventory inventory) {
        InventoryHolder owner = CatInventoryUtils.getOwner(inventory);
        return (owner == null ? new CatCustomInventory(inventory).getInventory() : owner.getInventory());
    }
}
