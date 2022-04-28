package catserver.server.entity;


import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftAbstractHorse;
import org.bukkit.entity.Horse;

public class CraftCustomChestHorse extends CraftAbstractHorse {

    public CraftCustomChestHorse(CraftServer server, AbstractHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomHorse";
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.MOD_CUSTOM;
    }
}
