package catserver.server.entity;

import catserver.server.BukkitInjector;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CraftCustomEntity extends CraftEntity {
    private String entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
        this.entityName = BukkitInjector.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public Entity getHandle() {
        return (Entity) entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    @Override
    public @NotNull EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.MOD_CUSTOM;
        }
    }

    public String getCustomName() {
        Component component = getHandle().getCustomName();

        if (component != null) {
            String name = component.getString();
            if (Strings.isNotEmpty(name)) {
                return name;
            }
        }

        return getHandle().getName().getString();
    }
}