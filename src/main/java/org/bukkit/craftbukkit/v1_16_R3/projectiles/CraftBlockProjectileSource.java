package org.bukkit.craftbukkit.v1_16_R3.projectiles;

import java.util.Random;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.item.ExperienceBottleEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionUtil;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource implements BlockProjectileSource {
    private final DispenserTileEntity dispenserBlock;

    public CraftBlockProjectileSource(DispenserTileEntity dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getLevel().getWorld().getBlockAt(dispenserBlock.getBlockPos().getX(), dispenserBlock.getBlockPos().getY(), dispenserBlock.getBlockPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from DispenserBlock.dispense()
        ProxyBlockSource isourceblock = new ProxyBlockSource((ServerWorld) dispenserBlock.getLevel(), dispenserBlock.getBlockPos());
        // Copied from DispenseTaskProjectile
        IPosition iposition = DispenserBlock.getDispensePosition(isourceblock);
        Direction enumdirection = (Direction) isourceblock.getBlockState().getValue(DispenserBlock.FACING);
        net.minecraft.world.World world = dispenserBlock.getLevel();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new SnowballEntity(world, iposition.x(), iposition.y(), iposition.z());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EggEntity(world, iposition.x(), iposition.y(), iposition.z());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EnderPearlEntity(world, null);
            launch.setPos(iposition.x(), iposition.y(), iposition.z());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new ExperienceBottleEntity(world, iposition.x(), iposition.y(), iposition.z());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new PotionEntity(world, iposition.x(), iposition.y(), iposition.z());
                ((PotionEntity) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new PotionEntity(world, iposition.x(), iposition.y(), iposition.z());
                ((PotionEntity) launch).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
        } else if (AbstractArrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new ArrowEntity(world, iposition.x(), iposition.y(), iposition.z());
                ((ArrowEntity) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new SpectralArrowEntity(world, iposition.x(), iposition.y(), iposition.z());
            } else {
                launch = new ArrowEntity(world, iposition.x(), iposition.y(), iposition.z());
            }
            ((AbstractArrowEntity) launch).pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
            ((AbstractArrowEntity) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.x() + (double) ((float) enumdirection.getStepX() * 0.3F);
            double d1 = iposition.y() + (double) ((float) enumdirection.getStepY() * 0.3F);
            double d2 = iposition.z() + (double) ((float) enumdirection.getStepZ() * 0.3F);
            Random random = world.random;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getStepX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getStepY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getStepZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new SmallFireballEntity(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = EntityType.WITHER_SKULL.create(world);
                launch.setPos(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((DamagingProjectileEntity) launch).xPower = d3 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).yPower = d4 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).zPower = d5 / d6 * 0.1D;
            } else {
                launch = EntityType.FIREBALL.create(world);
                launch.setPos(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((DamagingProjectileEntity) launch).xPower = d3 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).yPower = d4 / d6 * 0.1D;
                ((DamagingProjectileEntity) launch).zPower = d5 / d6 * 0.1D;
            }

            ((DamagingProjectileEntity) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof ProjectileEntity) {
            if (launch instanceof ThrowableEntity) {
                ((ThrowableEntity) launch).projectileSource = this;
            }
            // Values from DispenseTaskProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof PotionEntity || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseTask classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseTaskProjectile
            ((ProjectileEntity) launch).shoot((double) enumdirection.getStepX(), (double) ((float) enumdirection.getStepY() + 0.1F), (double) enumdirection.getStepZ(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.addFreshEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
