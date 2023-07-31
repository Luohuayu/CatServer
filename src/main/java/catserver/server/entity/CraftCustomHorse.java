package catserver.server.entity;

import moe.loliserver.BukkitInjector;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

public class CraftCustomHorse extends CraftAbstractHorse {

    public String entityName;

    public CraftCustomHorse(CraftServer server, AbstractHorseEntity entity) {
        super(server, entity);
        this.entityName = BukkitInjector.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public String toString() {
        return "CraftCustomHorse";
    }

    @Override
    public EntityType getType() {
        EntityType entityType = EntityType.fromName(this.entityName);
        if (entityType != null) {
            return entityType;
        } else {
            return EntityType.MOD_CUSTOM;
        }
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.MOD_CUSTOM;
    }
}
