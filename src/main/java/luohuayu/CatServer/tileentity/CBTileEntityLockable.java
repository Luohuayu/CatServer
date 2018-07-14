package luohuayu.CatServer.tileentity;

import luohuayu.CatServer.inventory.ICBLockableContainer;
import net.minecraft.tileentity.TileEntityLockable;

public abstract class CBTileEntityLockable extends TileEntityLockable implements ICBLockableContainer {
    // CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
