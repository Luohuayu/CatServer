package catserver.server.bukkit;

import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.craftbukkit.v1_18_R2.enchantments.CraftEnchantment;
import org.jetbrains.annotations.NotNull;

public class CraftCustomEnchantment extends CraftEnchantment {

    private final String name;

    public CraftCustomEnchantment(Enchantment target, String name) {
        super(target);
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return super.getName().startsWith("UNKNOWN_ENCHANT_") ? this.name : super.getName();
    }
}
