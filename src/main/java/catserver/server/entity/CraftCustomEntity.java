package catserver.server.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity extends CraftEntity {
    private String entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.world.entity.Entity entity) {
        super(server, entity);
        this.entityName = entity.getName().toString();
    }

    @Override
    public Entity getHandle() {
        return (Entity) entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    public EntityType getType() {
        // TODO: FoxServer - Inject type
        return EntityType.MOD_CUSTOM;
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