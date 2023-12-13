package catserver.server.entity;

import catserver.server.BukkitInjector;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

public class CraftCustomProjectile extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter = null;
    private boolean doesBounce;
    public String entityName;

    public CraftCustomProjectile(CraftServer server, Entity entity) {
        super(server, entity);
        this.entityName = BukkitInjector.entityTypeMap.get(entity.getType());
        if (entityName == null) {
            entityName = entity.getName().getString();
        }
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        this.shooter = shooter;
    }

    @Override
    public boolean doesBounce() {
        return doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
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

    @Override
    public String toString() {
        return "CraftCustomProjectile";
    }
}