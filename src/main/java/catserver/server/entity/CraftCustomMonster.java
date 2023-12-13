package catserver.server.entity;

import catserver.server.BukkitInjector;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftMonster;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomMonster extends CraftMonster {
    public String entityName;

    public CraftCustomMonster(CraftServer server, net.minecraft.world.entity.monster.Monster entity) {
        super(server, entity);
        this.entityName = BukkitInjector.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
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
    public String toString() {
        return "CraftCustomMonster";
    }
}
