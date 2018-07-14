package luohuayu.CatServer.inventory;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public abstract class CBContainer extends Container {

	public abstract InventoryView getBukkitView();
	
	// CraftBukkit start
    public boolean checkReachable = true;
    public void transferTo(CBContainer other, org.bukkit.craftbukkit.entity.CraftHumanEntity player) {
    	if(!isCBInventory(other) || !isCBInventory(this)) {
    		return;
    	}
    	InventoryView source = this.getBukkitView(), destination = other.getBukkitView();
    	((ICBInventory)((CraftInventory) source.getTopInventory()).getInventory()).onClose(player);
    	((ICBInventory)((CraftInventory) source.getBottomInventory()).getInventory()).onClose(player);
    	((ICBInventory)((CraftInventory) destination.getTopInventory()).getInventory()).onOpen(player);
    	((ICBInventory)((CraftInventory) destination.getBottomInventory()).getInventory()).onOpen(player);
    }
    // CraftBukkit end
    public boolean isCBInventory(Container c) {
    	if(!(c instanceof CBContainer)) {
    		return false;
    	}
    	return ((CraftInventory)((CBContainer) c).getBukkitView().getTopInventory()).getInventory() instanceof ICBInventory && ((CraftInventory)((CBContainer) c).getBukkitView().getBottomInventory()).getInventory() instanceof ICBInventory;
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return false;
	}

}
