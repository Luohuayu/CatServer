// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.projectiles;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.projectile.EntityWitherSkull;
import org.bukkit.entity.WitherSkull;
import net.minecraft.entity.projectile.EntitySmallFireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Fireball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Arrow;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.ThrownPotion;
import net.minecraft.entity.item.EntityExpBottle;
import org.bukkit.entity.ThrownExpBottle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import org.bukkit.entity.EnderPearl;
import net.minecraft.entity.projectile.EntityEgg;
import org.bukkit.entity.Egg;
import net.minecraft.entity.projectile.EntitySnowball;
import org.bukkit.entity.Snowball;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.bukkit.entity.Projectile;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityDispenser;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftBlockProjectileSource implements BlockProjectileSource
{
    private final TileEntityDispenser dispenserBlock;
    
    public CraftBlockProjectileSource(final TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }
    
    @Override
    public Block getBlock() {
        return this.dispenserBlock.getWorld().getWorld().getBlockAt(this.dispenserBlock.getPos().getX(), this.dispenserBlock.getPos().getY(), this.dispenserBlock.getPos().getZ());
    }
    
    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile) {
        return this.launchProjectile(projectile, (Vector)null);
    }
    
    @Override
    public <T extends Projectile> T launchProjectile(final Class<? extends T> projectile, final Vector velocity) {
        Validate.isTrue(this.getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        final BlockSourceImpl isourceblock = new BlockSourceImpl(this.dispenserBlock.getWorld(), this.dispenserBlock.getPos());
        final IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        final EnumFacing enumdirection = isourceblock.getBlockState().getValue((IProperty<EnumFacing>)BlockDispenser.FACING);
        final World world = this.dispenserBlock.getWorld();
        Entity launch = null;
        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
        }
        else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
        }
        else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, null);
            launch.setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        }
        else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        }
        else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.LINGERING_POTION, 1)));
            }
            else {
                launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            }
        }
        else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
                ((EntityTippedArrow)launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            }
            else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
            else {
                launch = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
            ((EntityArrow)launch).pickupStatus = EntityArrow.PickupStatus.ALLOWED;
            ((EntityArrow)launch).projectileSource = this;
        }
        else if (Fireball.class.isAssignableFrom(projectile)) {
            final double d0 = iposition.getX() + enumdirection.getFrontOffsetX() * 0.3f;
            final double d2 = iposition.getY() + enumdirection.getFrontOffsetY() * 0.3f;
            final double d3 = iposition.getZ() + enumdirection.getFrontOffsetZ() * 0.3f;
            final Random random = world.rand;
            final double d4 = random.nextGaussian() * 0.05 + enumdirection.getFrontOffsetX();
            final double d5 = random.nextGaussian() * 0.05 + enumdirection.getFrontOffsetY();
            final double d6 = random.nextGaussian() * 0.05 + enumdirection.getFrontOffsetZ();
            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, null, d0, d2, d3);
            }
            else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world);
                launch.setPosition(d0, d2, d3);
                final double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
                ((EntityFireball)launch).accelerationX = d4 / d7 * 0.1;
                ((EntityFireball)launch).accelerationY = d5 / d7 * 0.1;
                ((EntityFireball)launch).accelerationZ = d6 / d7 * 0.1;
            }
            else {
                launch = new EntityLargeFireball(world);
                launch.setPosition(d0, d2, d3);
                final double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6);
                ((EntityFireball)launch).accelerationX = d4 / d7 * 0.1;
                ((EntityFireball)launch).accelerationY = d5 / d7 * 0.1;
                ((EntityFireball)launch).accelerationZ = d6 / d7 * 0.1;
            }
            ((EntityFireball)launch).projectileSource = this;
        }
        Validate.notNull((Object)launch, "Projectile not supported");
        if (launch instanceof IProjectile) {
            if (launch instanceof EntityThrowable) {
                ((EntityThrowable)launch).projectileSource = this;
            }
            float a = 6.0f;
            float b = 1.1f;
            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                a *= 0.5f;
                b *= 1.25f;
            }
            ((IProjectile)launch).setThrowableHeading(enumdirection.getFrontOffsetX(), enumdirection.getFrontOffsetY() + 0.1f, enumdirection.getFrontOffsetZ(), b, a);
        }
        if (velocity != null) {
            ((Projectile)launch.getBukkitEntity()).setVelocity(velocity);
        }
        world.spawnEntityInWorld(launch);
        return (T)launch.getBukkitEntity();
    }
}
