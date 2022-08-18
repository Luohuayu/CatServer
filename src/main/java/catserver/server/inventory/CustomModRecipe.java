package catserver.server.inventory;

import net.minecraft.resources.ResourceLocation;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CustomModRecipe implements Recipe, Keyed {
    private final net.minecraft.world.item.crafting.Recipe iRecipe;
    private final ItemStack output;
    private NamespacedKey key;

    public CustomModRecipe(net.minecraft.world.item.crafting.Recipe iRecipe){
        this(iRecipe, null);
    }

    public CustomModRecipe(net.minecraft.world.item.crafting.Recipe iRecipe, ResourceLocation key){
        this.iRecipe = iRecipe;
        this.output = CraftItemStack.asCraftMirror(iRecipe.getResultItem());
        try {
            this.key = (key != null ? CraftNamespacedKey.fromMinecraft(key) : NamespacedKey.randomKey());
        } catch (Exception e) {
            this.key = NamespacedKey.randomKey();
        }
    }

    @Override
    public ItemStack getResult() {
        return output.clone();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public net.minecraft.world.item.crafting.Recipe getHandle(){
        return iRecipe;
    }

}
