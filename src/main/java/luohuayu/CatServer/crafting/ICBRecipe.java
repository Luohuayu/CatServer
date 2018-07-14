package luohuayu.CatServer.crafting;

import net.minecraft.item.crafting.IRecipe;

public interface ICBRecipe extends IRecipe {
	org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit
}
