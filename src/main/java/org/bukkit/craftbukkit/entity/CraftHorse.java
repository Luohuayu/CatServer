// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.inventory.Inventory;
import org.bukkit.entity.EntityType;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryHorse;
import org.bukkit.inventory.HorseInventory;
import java.util.UUID;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.entity.EntityLivingBase;
import org.bukkit.entity.AnimalTamer;
import net.minecraft.entity.passive.HorseType;
import org.apache.commons.lang.Validate;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Horse;

public class CraftHorse extends CraftAnimals implements Horse
{
    public CraftHorse(final CraftServer server, final EntityHorse entity) {
        super(server, entity);
    }
    
    @Override
    public EntityHorse getHandle() {
        return (EntityHorse)this.entity;
    }
    
    @Override
    public Variant getVariant() {
        return Variant.values()[this.getHandle().getType().ordinal()];
    }
    
    @Override
    public void setVariant(final Variant variant) {
        Validate.notNull((Object)variant, "Variant cannot be null");
        this.getHandle().setType(HorseType.values()[variant.ordinal()]);
    }
    
    @Override
    public Color getColor() {
        return Color.values()[this.getHandle().getHorseVariant() & 0xFF];
    }
    
    @Override
    public void setColor(final Color color) {
        Validate.notNull((Object)color, "Color cannot be null");
        this.getHandle().setHorseVariant((color.ordinal() & 0xFF) | this.getStyle().ordinal() << 8);
    }
    
    @Override
    public Style getStyle() {
        return Style.values()[this.getHandle().getHorseVariant() >>> 8];
    }
    
    @Override
    public void setStyle(final Style style) {
        Validate.notNull((Object)style, "Style cannot be null");
        this.getHandle().setHorseVariant((this.getColor().ordinal() & 0xFF) | style.ordinal() << 8);
    }
    
    @Override
    public boolean isCarryingChest() {
        return this.getHandle().isChested();
    }
    
    @Override
    public void setCarryingChest(final boolean chest) {
        if (chest == this.isCarryingChest()) {
            return;
        }
        this.getHandle().setChested(chest);
        this.getHandle().initHorseChest();
    }
    
    @Override
    public int getDomestication() {
        return this.getHandle().getTemper();
    }
    
    @Override
    public void setDomestication(final int value) {
        Validate.isTrue(value >= 0, "Domestication cannot be less than zero");
        Validate.isTrue(value <= this.getMaxDomestication(), "Domestication cannot be greater than the max domestication");
        this.getHandle().setTemper(value);
    }
    
    @Override
    public int getMaxDomestication() {
        return this.getHandle().getMaxTemper();
    }
    
    @Override
    public void setMaxDomestication(final int value) {
        Validate.isTrue(value > 0, "Max domestication cannot be zero or less");
        this.getHandle().maxDomestication = value;
    }
    
    @Override
    public double getJumpStrength() {
        return this.getHandle().getHorseJumpStrength();
    }
    
    @Override
    public void setJumpStrength(final double strength) {
        Validate.isTrue(strength >= 0.0, "Jump strength cannot be less than zero");
        this.getHandle().getEntityAttribute(EntityHorse.JUMP_STRENGTH).setBaseValue(strength);
    }
    
    @Override
    public boolean isTamed() {
        return this.getHandle().isTame();
    }
    
    @Override
    public void setTamed(final boolean tamed) {
        this.getHandle().setHorseTamed(tamed);
    }
    
    @Override
    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        return this.getServer().getOfflinePlayer(this.getOwnerUUID());
    }
    
    @Override
    public void setOwner(final AnimalTamer owner) {
        if (owner != null) {
            this.setTamed(true);
            this.getHandle().setGoalTarget(null, null, false);
            this.setOwnerUUID(owner.getUniqueId());
        }
        else {
            this.setTamed(false);
            this.setOwnerUUID(null);
        }
    }
    
    public UUID getOwnerUUID() {
        return this.getHandle().getOwnerUniqueId();
    }
    
    public void setOwnerUUID(final UUID uuid) {
        this.getHandle().setOwnerUniqueId(uuid);
    }
    
    @Override
    public HorseInventory getInventory() {
        return new CraftInventoryHorse(this.getHandle().horseChest);
    }
    
    @Override
    public String toString() {
        return "CraftHorse{variant=" + this.getVariant() + ", owner=" + this.getOwner() + '}';
    }
    
    @Override
    public EntityType getType() {
        return EntityType.HORSE;
    }
}
