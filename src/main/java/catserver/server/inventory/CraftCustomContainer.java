package catserver.server.inventory;

import net.minecraft.world.Container;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
    private final CraftWorld world;
    private final Container inventory;

    public CraftCustomContainer(Block block) {
        super(block);
        world = (CraftWorld) block.getWorld();
        inventory = (Container) world.getTileEntityAt(getX(), getY(), getZ());
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.inventory);
    }

}
