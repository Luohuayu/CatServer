package catserver.server.entity;

import catserver.server.BukkitInjector;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftAbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.jetbrains.annotations.NotNull;

public class CraftCustomChestHorse extends CraftAbstractHorse {

    public String entityName;

    public CraftCustomChestHorse(CraftServer server, AbstractHorse entity) {
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
    public @NotNull EntityType getType() {
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
