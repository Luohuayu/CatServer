package catserver.server.entity;

import catserver.server.BukkitInjector;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftAbstractSkeleton;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.jetbrains.annotations.NotNull;

public class CraftCustomAbstractSkeleton extends CraftAbstractSkeleton {

    public String entityName;

    public CraftCustomAbstractSkeleton(CraftServer server, AbstractSkeleton entity) {
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
    public @NotNull Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.MOD_CUSTOM;
    }
}
