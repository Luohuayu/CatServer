package catserver.server.inventory;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CustomModRecipe implements Recipe, Keyed {
    private final IRecipe iRecipe;
    private final ItemStack output;
    private NamespacedKey key;

    public CustomModRecipe(IRecipe iRecipe){
        this(iRecipe, null);
    }

    public CustomModRecipe(IRecipe iRecipe, ResourceLocation key){
        this.iRecipe = iRecipe;
        this.output = CraftItemStack.asCraftMirror(iRecipe.getRecipeOutput());
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
    
    public IRecipe getHandle(){
        return iRecipe;
    }

}
