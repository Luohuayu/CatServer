package org.bukkit.craftbukkit.v1_16_R3.entity;

import java.util.UUID;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.AbstractHorseInventory;

public abstract class CraftAbstractHorse extends CraftAnimals implements AbstractHorse {

    public CraftAbstractHorse(CraftServer server, AbstractHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractHorseEntity getHandle() {
        return (AbstractHorseEntity) entity;
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getDomestication() {
        return getHandle().getTemper();
    }

    @Override
    public void setDomestication(int value) {
        Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
        Validate.isTrue(value <= getMaxDomestication(), "Domestication cannot be greater than the max domestication");
        getHandle().setTemper(value);
    }

    @Override
    public int getMaxDomestication() {
        return getHandle().getMaxTemper();
    }

    @Override
    public void setMaxDomestication(int value) {
        Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
        getHandle().maxDomestication = value;
    }

    @Override
    public double getJumpStrength() {
        return getHandle().getCustomJump();
    }

    @Override
    public void setJumpStrength(double strength) {
        Validate.isTrue(strength >= 0, "Jump strength cannot be less than zero");
        getHandle().getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(strength);
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTamed();
    }

    @Override
    public void setTamed(boolean tamed) {
        getHandle().setTamed(tamed);
    }

    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) return null;
        return getServer().getOfflinePlayer(getOwnerUUID());
    }

    @Override
    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            setTamed(true);
            getHandle().setGoalTarget(null, null, false);
            setOwnerUUID(owner.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    public UUID getOwnerUUID() {
        return getHandle().getOwnerUUID();
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerUUID(uuid);
    }

    @Override
    public AbstractHorseInventory getInventory() {
        return new CraftInventoryAbstractHorse(getHandle().inventory);
    }
}
