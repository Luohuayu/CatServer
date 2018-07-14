package luohuayu.CatServer.tileentity;

import luohuayu.CatServer.inventory.ICBInventory;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;

public abstract class CBTileEntityLockableLoot extends TileEntityLockableLoot implements ICBInventory {
	// CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
