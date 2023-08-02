package catserver.server.bukkit;

import net.minecraft.potion.Effect;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomPotionEffectType extends CraftPotionEffectType {

    private final String name;

    public CraftCustomPotionEffectType(Effect handle, String name) {
        super(handle);
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return super.getName().startsWith("UNKNOWN_EFFECT_TYPE_") ? this.name : super.getName();
    }
}
