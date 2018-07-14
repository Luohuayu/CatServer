package luohuayu.CatServer.inventory;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface ICBInventory extends IInventory {
	// CraftBukkit start
	ItemStack[] getContents();
	
	void onOpen(CraftHumanEntity who);
	
	void onClose(CraftHumanEntity who);
	
	java.util.List<org.bukkit.entity.HumanEntity> getViewers();
	
	org.bukkit.inventory.InventoryHolder getOwner();
	
	void setMaxStackSize(int size);
	
	org.bukkit.Location getLocation();
	
	int MAX_STACK = 64;
	// CraftBukkit end
}
