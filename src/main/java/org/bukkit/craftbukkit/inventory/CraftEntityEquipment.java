package org.bukkit.craftbukkit.inventory;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        setEquipment(EntityEquipmentSlot.MAINHAND, item);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(EntityEquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        setEquipment(EntityEquipmentSlot.OFFHAND, item);
    }

    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItemInMainHand(stack);
    }

    public ItemStack getHelmet() {
        return getEquipment(EntityEquipmentSlot.HEAD);
    }

    public void setHelmet(ItemStack helmet) {
        setEquipment(EntityEquipmentSlot.HEAD, helmet);
    }

    public ItemStack getChestplate() {
        return getEquipment(EntityEquipmentSlot.CHEST);
    }

    public void setChestplate(ItemStack chestplate) {
        setEquipment(EntityEquipmentSlot.CHEST, chestplate);
    }

    public ItemStack getLeggings() {
        return getEquipment(EntityEquipmentSlot.LEGS);
    }

    public void setLeggings(ItemStack leggings) {
        setEquipment(EntityEquipmentSlot.LEGS, leggings);
    }

    public ItemStack getBoots() {
        return getEquipment(EntityEquipmentSlot.FEET);
    }

    public void setBoots(ItemStack boots) {
        setEquipment(EntityEquipmentSlot.FEET, boots);
    }

    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(EntityEquipmentSlot.FEET),
                getEquipment(EntityEquipmentSlot.LEGS),
                getEquipment(EntityEquipmentSlot.CHEST),
                getEquipment(EntityEquipmentSlot.HEAD),
        };
        return armor;
    }

    public void setArmorContents(ItemStack[] items) {
        setEquipment(EntityEquipmentSlot.FEET, items.length >= 1 ? items[0] : null);
        setEquipment(EntityEquipmentSlot.LEGS, items.length >= 2 ? items[1] : null);
        setEquipment(EntityEquipmentSlot.CHEST, items.length >= 3 ? items[2] : null);
        setEquipment(EntityEquipmentSlot.HEAD, items.length >= 4 ? items[3] : null);
    }

    private ItemStack getEquipment(EntityEquipmentSlot slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getItemStackFromSlot(slot));
    }

    private void setEquipment(EntityEquipmentSlot slot, ItemStack stack) {
        entity.getHandle().setItemStackToSlot(slot, CraftItemStack.asNMSCopy(stack));
    }

    public void clear() {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            setEquipment(slot, null);
        }
    }

    public Entity getHolder() {
        return entity;
    }

    @Override
    public float getItemInHandDropChance() {
        return getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
       return getDropChance(EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(EntityEquipmentSlot.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.OFFHAND, chance);
    }

    public float getHelmetDropChance() {
        return getDropChance(EntityEquipmentSlot.HEAD);
    }

    public void setHelmetDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.HEAD, chance);
    }

    public float getChestplateDropChance() {
        return getDropChance(EntityEquipmentSlot.CHEST);
    }

    public void setChestplateDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.CHEST, chance);
    }

    public float getLeggingsDropChance() {
        return getDropChance(EntityEquipmentSlot.LEGS);
    }

    public void setLeggingsDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.LEGS, chance);
    }

    public float getBootsDropChance() {
        return getDropChance(EntityEquipmentSlot.FEET);
    }

    public void setBootsDropChance(float chance) {
        setDropChance(EntityEquipmentSlot.FEET, chance);
    }

    private void setDropChance(EntityEquipmentSlot slot, float chance) {
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            ((EntityLiving) entity.getHandle()).inventoryHandsDropChances[slot.getIndex()] = chance - 0.1F;
        } else {
            ((EntityLiving) entity.getHandle()).inventoryArmorDropChances[slot.getIndex()] = chance - 0.1F;
        }
    }

    private float getDropChance(EntityEquipmentSlot slot) {
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            return ((EntityLiving) entity.getHandle()).inventoryHandsDropChances[slot.getIndex()] + 0.1F;
        } else {
            return ((EntityLiving) entity.getHandle()).inventoryArmorDropChances[slot.getIndex()] + 0.1F;
        }
    }
}
