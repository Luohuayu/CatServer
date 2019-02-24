package org.bukkit.craftbukkit.projectiles;

import java.util.Random;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
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
    private final TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    @Override
    public Block getBlock() {
        return dispenserBlock.getWorld().getWorld().getBlockAt(dispenserBlock.getPos().getX(), dispenserBlock.getPos().getY(), dispenserBlock.getPos().getZ());
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        Validate.isTrue(getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        // Copied from BlockDispenser.dispense()
        IBlockSource isourceblock = new BlockSourceImpl(dispenserBlock.getWorld(), dispenserBlock.getPos());
        // Copied from DispenseBehaviorProjectile
        IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
        net.minecraft.world.World world = dispenserBlock.getWorld();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, null);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
            } else {
                launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            }
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((EntityTippedArrow) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            } else {
                launch = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
            ((EntityArrow) launch).pickupStatus = EntityArrow.PickupStatus.ALLOWED;
            ((EntityArrow) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getFrontOffsetX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getFrontOffsetY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getFrontOffsetZ() * 0.3F);
            Random random = world.rand;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, null, d0, d1, d2);
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                ((EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                ((EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
            } else {
                launch = new EntityLargeFireball(world);
                launch.setPosition(d0, d1, d2);
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                ((EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                ((EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                ((EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
            }

            ((EntityFireball) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");

        if (launch instanceof IProjectile) {
            if (launch instanceof EntityThrowable) {
                ((EntityThrowable) launch).projectileSource = this;
            }
            // Values from DispenseBehaviorProjectile
            float a = 6.0F;
            float b = 1.1F;
            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                // Values from respective DispenseBehavior classes
                a *= 0.5F;
                b *= 1.25F;
            }
            // Copied from DispenseBehaviorProjectile
            ((IProjectile) launch).shoot((double) enumdirection.getFrontOffsetX(), (double) ((float) enumdirection.getFrontOffsetY() + 0.1F), (double) enumdirection.getFrontOffsetZ(), b, a);
        }

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.spawnEntity(launch);
        return (T) launch.getBukkitEntity();
    }
}
