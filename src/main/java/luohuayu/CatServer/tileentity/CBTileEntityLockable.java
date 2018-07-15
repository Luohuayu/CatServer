package luohuayu.CatServer.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;

public abstract class CBTileEntityLockable extends TileEntityLockable implements IInventory, ILockableContainer, IInteractionObject {
    // CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
    	return new org.bukkit.Location(worldObj.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}