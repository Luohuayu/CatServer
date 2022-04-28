package catserver.server.entity;

import moe.loliserver.BukkitInjector;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftProjectile;
import org.bukkit.entity.EntityType;

public class CraftCustomProjectile extends CraftProjectile {
    public String entityName;

    public CraftCustomProjectile(CraftServer server, ProjectileEntity entity) {
        super(server, entity);
        this.entityName = BukkitInjector.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public EntityType getType() {
        EntityType type = EntityType.fromName(this.entityName);
        if (type != null) {
            return type;
        } else {
            return EntityType.MOD_CUSTOM;
        }
    }

    @Override
    public String toString()
    {
        return "CustomProjectileEntity";
    }
}

