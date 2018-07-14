// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.EntityLiving;
import org.bukkit.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.inventory.EntityEquipment;

public class CraftEntityEquipment implements EntityEquipment
{
    private final CraftLivingEntity entity;
    
    public CraftEntityEquipment(final CraftLivingEntity entity) {
        this.entity = entity;
    }
    
    @Override
    public ItemStack getItemInMainHand() {
        return this.getEquipment(EntityEquipmentSlot.MAINHAND);
    }
    
    @Override
    public void setItemInMainHand(final ItemStack item) {
        this.setEquipment(EntityEquipmentSlot.MAINHAND, item);
    }
    
    @Override
    public ItemStack getItemInOffHand() {
        return this.getEquipment(EntityEquipmentSlot.OFFHAND);
    }
    
    @Override
    public void setItemInOffHand(final ItemStack item) {
        this.setEquipment(EntityEquipmentSlot.OFFHAND, item);
    }
    
    @Override
    public ItemStack getItemInHand() {
        return this.getItemInMainHand();
    }
    
    @Override
    public void setItemInHand(final ItemStack stack) {
        this.setItemInMainHand(stack);
    }
    
    @Override
    public ItemStack getHelmet() {
        return this.getEquipment(EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void setHelmet(final ItemStack helmet) {
        this.setEquipment(EntityEquipmentSlot.HEAD, helmet);
    }
    
    @Override
    public ItemStack getChestplate() {
        return this.getEquipment(EntityEquipmentSlot.CHEST);
    }
    
    @Override
    public void setChestplate(final ItemStack chestplate) {
        this.setEquipment(EntityEquipmentSlot.CHEST, chestplate);
    }
    
    @Override
    public ItemStack getLeggings() {
        return this.getEquipment(EntityEquipmentSlot.LEGS);
    }
    
    @Override
    public void setLeggings(final ItemStack leggings) {
        this.setEquipment(EntityEquipmentSlot.LEGS, leggings);
    }
    
    @Override
    public ItemStack getBoots() {
        return this.getEquipment(EntityEquipmentSlot.FEET);
    }
    
    @Override
    public void setBoots(final ItemStack boots) {
        this.setEquipment(EntityEquipmentSlot.FEET, boots);
    }
    
    @Override
    public ItemStack[] getArmorContents() {
        final ItemStack[] armor = { this.getEquipment(EntityEquipmentSlot.FEET), this.getEquipment(EntityEquipmentSlot.LEGS), this.getEquipment(EntityEquipmentSlot.CHEST), this.getEquipment(EntityEquipmentSlot.HEAD) };
        return armor;
    }
    
    @Override
    public void setArmorContents(final ItemStack[] items) {
        this.setEquipment(EntityEquipmentSlot.FEET, (items.length >= 1) ? items[0] : null);
        this.setEquipment(EntityEquipmentSlot.LEGS, (items.length >= 2) ? items[1] : null);
        this.setEquipment(EntityEquipmentSlot.CHEST, (items.length >= 3) ? items[2] : null);
        this.setEquipment(EntityEquipmentSlot.HEAD, (items.length >= 4) ? items[3] : null);
    }
    
    private ItemStack getEquipment(final EntityEquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(this.entity.getHandle().getItemStackFromSlot(slot));
    }
    
    private void setEquipment(final EntityEquipmentSlot slot, final ItemStack stack) {
        this.entity.getHandle().setItemStackToSlot(slot, CraftItemStack.asNMSCopy(stack));
    }
    
    @Override
    public void clear() {
        EntityEquipmentSlot[] values;
        for (int length = (values = EntityEquipmentSlot.values()).length, i = 0; i < length; ++i) {
            final EntityEquipmentSlot slot = values[i];
            this.setEquipment(slot, null);
        }
    }
    
    @Override
    public Entity getHolder() {
        return this.entity;
    }
    
    @Override
    public float getItemInHandDropChance() {
        return this.getItemInMainHandDropChance();
    }
    
    @Override
    public void setItemInHandDropChance(final float chance) {
        this.setItemInMainHandDropChance(chance);
    }
    
    @Override
    public float getItemInMainHandDropChance() {
        return this.getDropChance(EntityEquipmentSlot.MAINHAND);
    }
    
    @Override
    public void setItemInMainHandDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.MAINHAND, chance);
    }
    
    @Override
    public float getItemInOffHandDropChance() {
        return this.getDropChance(EntityEquipmentSlot.OFFHAND);
    }
    
    @Override
    public void setItemInOffHandDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.OFFHAND, chance);
    }
    
    @Override
    public float getHelmetDropChance() {
        return this.getDropChance(EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void setHelmetDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.HEAD, chance);
    }
    
    @Override
    public float getChestplateDropChance() {
        return this.getDropChance(EntityEquipmentSlot.CHEST);
    }
    
    @Override
    public void setChestplateDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.CHEST, chance);
    }
    
    @Override
    public float getLeggingsDropChance() {
        return this.getDropChance(EntityEquipmentSlot.LEGS);
    }
    
    @Override
    public void setLeggingsDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.LEGS, chance);
    }
    
    @Override
    public float getBootsDropChance() {
        return this.getDropChance(EntityEquipmentSlot.FEET);
    }
    
    @Override
    public void setBootsDropChance(final float chance) {
        this.setDropChance(EntityEquipmentSlot.FEET, chance);
    }
    
    private void setDropChance(final EntityEquipmentSlot slot, final float chance) {
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            ((EntityLiving)this.entity.getHandle()).inventoryHandsDropChances[slot.getIndex()] = chance - 0.1f;
        }
        else {
            ((EntityLiving)this.entity.getHandle()).inventoryArmorDropChances[slot.getIndex()] = chance - 0.1f;
        }
    }
    
    private float getDropChance(final EntityEquipmentSlot slot) {
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            return ((EntityLiving)this.entity.getHandle()).inventoryHandsDropChances[slot.getIndex()] + 0.1f;
        }
        return ((EntityLiving)this.entity.getHandle()).inventoryArmorDropChances[slot.getIndex()] + 0.1f;
    }
}
