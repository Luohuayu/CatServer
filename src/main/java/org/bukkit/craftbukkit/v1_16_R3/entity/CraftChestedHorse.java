package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.ChestedHorse;

public abstract class CraftChestedHorse extends CraftAbstractHorse implements ChestedHorse {

    public CraftChestedHorse(CraftServer server, AbstractChestedHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractChestedHorseEntity getHandle() {
        return (AbstractChestedHorseEntity) super.getHandle();
    }

    @Override
    public boolean isCarryingChest() {
        return getHandle().hasChest();
    }

    @Override
    public void setCarryingChest(boolean chest) {
        if (chest == isCarryingChest()) return;
        getHandle().setChest(chest);
        getHandle().createInventory();
    }
}
