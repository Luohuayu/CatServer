package catserver.server.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;

public class CraftCustomEntity extends CraftEntity {
    private String entityName;

    public CraftCustomEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityName = EntityRegistry.entityTypeMap.get(entity.getClass());
        if (entityName == null)
            entityName = entity.getName();
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
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null)
            return type;
        else return EntityType.MOD_CUSTOM;
    }

    public String getCustomName() {
        String name = getHandle().getCustomNameTag();

        if (name == null || name.length() == 0) {
            return this.entity.getName();
        }

        return name;
    }
}