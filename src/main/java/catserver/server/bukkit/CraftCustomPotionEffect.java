package catserver.server.bukkit;

import net.minecraft.world.effect.MobEffect;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomPotionEffect extends CraftPotionEffectType {

    private final String name;

    public CraftCustomPotionEffect(MobEffect handle, String name) {
        super(handle);
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return super.getName().startsWith("UNKNOWN_EFFECT_TYPE_") ? this.name : super.getName();
    }
}
