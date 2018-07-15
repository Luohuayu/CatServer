package luohuayu.CatServer.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;

import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CraftCustomEntity extends CraftEntity {
    public Class<? extends net.minecraft.entity.Entity> entityClass;
    public String entityName;

    public CraftCustomEntity(final CraftServer server,final net.minecraft.entity.Entity entity) {
        super(server, entity);
        this.entityClass = entity.getClass();
        this.entityName = EntityRegistry.getCustomEntityTypeName(entityClass);
        if (entityName == null)
            entityName = entity.getCommandSenderEntity().getName();

    }

    @Override
    public net.minecraft.entity.Entity getHandle() {
        return (net.minecraft.entity.Entity) entity;
    }

    @Override
    public String toString() {
        return this.entityName;
    }

    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null)
            return type;
        else return EntityType.UNKNOWN;
    }
}
