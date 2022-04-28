package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.projectile.TridentEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class CraftTrident extends CraftArrow implements Trident {

    public CraftTrident(CraftServer server, TridentEntity entity) {
        super(server, entity);
    }

    @Override
    public TridentEntity getHandle() {
        return (TridentEntity) super.getHandle();
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

    @Override
    public EntityType getType() {
        return EntityType.TRIDENT;
    }
}
