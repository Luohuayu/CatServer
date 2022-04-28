package catserver.server.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
    private final CraftWorld world;
    private final IInventory inventory;

    public CraftCustomContainer(Block block) {
        super(block);
        world = (CraftWorld) block.getWorld();
        inventory = (IInventory)world.getTileEntityAt(getX(), getY(), getZ());
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.inventory);
    }

}
