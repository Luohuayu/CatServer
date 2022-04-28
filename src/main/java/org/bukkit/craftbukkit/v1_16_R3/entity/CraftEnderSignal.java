package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, EyeOfEnderEntity entity) {
        super(server, entity);
    }

    @Override
    public EyeOfEnderEntity getHandle() {
        return (EyeOfEnderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }

    @Override
    public Location getTargetLocation() {
        return new Location(getWorld(), getHandle().tx, getHandle().ty, getHandle().tz, getHandle().yRot, getHandle().xRot);
    }

    @Override
    public void setTargetLocation(Location location) {
        Preconditions.checkArgument(getWorld().equals(location.getWorld()), "Cannot target EnderSignal across worlds");
        getHandle().signalTo(new BlockPos(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public boolean getDropItem() {
        return getHandle().surviveAfterDeath;
    }

    @Override
    public void setDropItem(boolean shouldDropItem) {
        getHandle().surviveAfterDeath = shouldDropItem;
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(item != null ? CraftItemStack.asNMSCopy(item) : Items.ENDER_EYE.getDefaultInstance());
    }

    @Override
    public int getDespawnTimer() {
        return getHandle().life;
    }

    @Override
    public void setDespawnTimer(int time) {
        getHandle().life = time;
    }
}
