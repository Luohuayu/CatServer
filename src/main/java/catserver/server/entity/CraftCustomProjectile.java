package catserver.server.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class CraftCustomProjectile extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter = null;
    private boolean doesBounce;

    public CraftCustomProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public static final GameProfile dropper =  new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");

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
    public String toString() {
        return "CraftCustomProjectile";
    }
}