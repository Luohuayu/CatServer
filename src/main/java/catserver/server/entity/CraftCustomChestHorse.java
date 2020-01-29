package catserver.server.entity;

import net.minecraft.entity.passive.AbstractChestHorse;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChestedHorse;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftChestedHorse {

    public CraftCustomChestHorse(CraftServer server, AbstractChestHorse entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomChestHorse";
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.MOD_CUSTOM;
    }
}
