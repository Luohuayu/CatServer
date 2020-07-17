package catserver.server.inventory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import org.bukkit.Material;

public class BukkitMaterialHelper {
    public static Material convertModItemMaterialToBlock(Material material) {
        Item nmsItem = Item.getItemById(material.getId());
        if (nmsItem instanceof ItemBlock) {
            int blockID = Block.getIdFromBlock(((ItemBlock)nmsItem).getBlock());
            if (blockID > 0) {
                return Material.getBlockMaterial(blockID);
            }
        }
        return material;
    }
}
