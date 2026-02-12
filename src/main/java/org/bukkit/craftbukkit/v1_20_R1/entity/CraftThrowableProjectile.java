package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;

public abstract class CraftThrowableProjectile extends CraftProjectile implements ThrowableProjectile {

    public CraftThrowableProjectile(CraftServer server, ThrowableItemProjectile entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getItem() {
        if (getHandle().getItemRaw().isEmpty()) {
            return CraftItemStack.asBukkitCopy(new net.minecraft.world.item.ItemStack(getHandle().getDefaultItemPublic()));
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().getItemRaw());
        }
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ThrowableItemProjectile getHandle() {
        return (ThrowableItemProjectile) entity;
    }
}
