package luohuayu.CatServer.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityLockableLoot;

public abstract class CBTileEntityLockableLoot extends TileEntityLockableLoot implements IInventory {
	// CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
