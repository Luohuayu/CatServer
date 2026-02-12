package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.projectile.ThrownTrident;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class CraftTrident extends CraftArrow implements Trident {

    public CraftTrident(CraftServer server, ThrownTrident entity) {
        super(server, entity);
    }

    @Override
    public ThrownTrident getHandle() {
        return (ThrownTrident) super.getHandle();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().tridentItem);
    }

    @Override
    public void setItem(ItemStack itemStack) {
        getHandle().tridentItem = CraftItemStack.asNMSCopy(itemStack);
    }

    @Override
    public String toString() {
        return "CraftTrident";
    }
}
