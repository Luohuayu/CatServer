package luohuayu.CatServer.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.WorldServer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class CustomCraftProjectile extends CraftCustomEntity implements Projectile {
    private ProjectileSource shooter = null;
    private boolean doesBounce;
    public CustomCraftProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }
    public static final GameProfile dropper =  new GameProfile(UUID.nameUUIDFromBytes("[Dropper]".getBytes()), "[Dropper]");
    @Override
    public LivingEntity _INVALID_getShooter() {
        if (shooter instanceof LivingEntity) { return (LivingEntity)shooter; }
        if (shooter instanceof BlockProjectileSource)
        {
            Block block = ((BlockProjectileSource)shooter).getBlock();
            if(!(block.getWorld() instanceof WorldServer))return null;
            int x = block.getX(), y = block.getY(), z = block.getZ();
            WorldServer ws = (WorldServer)block.getWorld();
            EntityPlayerMP fake_dropper = new EntityPlayerMP(FMLCommonHandler.instance().getMinecraftServerInstance().getServer(), ws, dropper, new PlayerInteractionManager(FMLCommonHandler.instance().getMinecraftServerInstance().getServer().worldServerForDimension(0)));
            fake_dropper.posX = x; fake_dropper.posY = y; fake_dropper.posZ = z;
            CraftEntity ce = org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, fake_dropper);
            if(ce instanceof LivingEntity) return (LivingEntity)ce;
            return null;
        } return null;
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void _INVALID_setShooter(LivingEntity living) {
        if (living instanceof ProjectileSource) { this.shooter = (ProjectileSource) living; }
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
    public String toString()
    {
        return "CraftCustomProjectile";
    }
}
