package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.ProjectileItemEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;

public abstract class CraftThrowableProjectile extends CraftProjectile implements ThrowableProjectile {

    public CraftThrowableProjectile(CraftServer server, ProjectileItemEntity entity) {
        super(server, entity);
    }

    @Override
    public ItemStack getItem() {
        if (getHandle().getItemRaw().isEmpty()) {
            return CraftItemStack.asBukkitCopy(new net.minecraft.item.ItemStack(getHandle().getDefaultItemPublic()));
        } else {
            return CraftItemStack.asBukkitCopy(getHandle().getItemRaw());
        }
    }

    @Override
    public void setItem(ItemStack item) {
        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public ProjectileItemEntity getHandle() {
        return (ProjectileItemEntity) entity;
    }
}
