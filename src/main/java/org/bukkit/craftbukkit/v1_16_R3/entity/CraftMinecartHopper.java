package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.item.minecart.HopperMinecartEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;

public final class CraftMinecartHopper extends CraftMinecartContainer implements HopperMinecart {
    private final CraftInventory inventory;

    public CraftMinecartHopper(CraftServer server, HopperMinecartEntity entity) {
        super(server, entity);
        inventory = new CraftInventory(entity);
    }

    @Override
    public String toString() {
        return "CraftMinecartHopper{" + "inventory=" + inventory + '}';
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART_HOPPER;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isEnabled() {
        return ((HopperMinecartEntity) getHandle()).isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ((HopperMinecartEntity) getHandle()).setEnabled(enabled);
    }
}
