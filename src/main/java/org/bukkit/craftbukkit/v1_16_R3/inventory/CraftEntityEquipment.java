package org.bukkit.craftbukkit.v1_16_R3.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import org.bukkit.craftbukkit.v1_16_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftEntityEquipment implements EntityEquipment {

    private final CraftLivingEntity entity;

    public CraftEntityEquipment(CraftLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item) {
        this.setItem(slot, item, false);
    }

    @Override
    public void setItem(EquipmentSlot slot, ItemStack item, boolean silent) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        EquipmentSlotType nmsSlot = CraftEquipmentSlot.getNMS(slot);
        setEquipment(nmsSlot, item, silent);
    }

    @Override
    public ItemStack getItem(EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        EquipmentSlotType nmsSlot = CraftEquipmentSlot.getNMS(slot);
        return getEquipment(nmsSlot);
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getEquipment(EquipmentSlotType.MAINHAND);
    }

    @Override
    public void setItemInMainHand(ItemStack item) {
        this.setItemInMainHand(item, false);
    }

    @Override
    public void setItemInMainHand(ItemStack item, boolean silent) {
        setEquipment(EquipmentSlotType.MAINHAND, item, silent);
    }

    @Override
    public ItemStack getItemInOffHand() {
        return getEquipment(EquipmentSlotType.OFFHAND);
    }

    @Override
    public void setItemInOffHand(ItemStack item) {
        this.setItemInOffHand(item, false);
    }

    @Override
    public void setItemInOffHand(ItemStack item, boolean silent) {
        setEquipment(EquipmentSlotType.OFFHAND, item, silent);
    }

    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItemInMainHand(stack);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment(EquipmentSlotType.HEAD);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        this.setHelmet(helmet, false);
    }

    @Override
    public void setHelmet(ItemStack helmet, boolean silent) {
        setEquipment(EquipmentSlotType.HEAD, helmet, silent);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment(EquipmentSlotType.CHEST);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        this.setChestplate(chestplate, false);
    }

    @Override
    public void setChestplate(ItemStack chestplate, boolean silent) {
        setEquipment(EquipmentSlotType.CHEST, chestplate, silent);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment(EquipmentSlotType.LEGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        this.setLeggings(leggings, false);
    }

    @Override
    public void setLeggings(ItemStack leggings, boolean silent) {
        setEquipment(EquipmentSlotType.LEGS, leggings, silent);
    }

    @Override
    public ItemStack getBoots() {
        return getEquipment(EquipmentSlotType.FEET);
    }

    @Override
    public void setBoots(ItemStack boots) {
        this.setBoots(boots, false);
    }

    @Override
    public void setBoots(ItemStack boots, boolean silent) {
        setEquipment(EquipmentSlotType.FEET, boots, silent);
    }

    @Override
    public ItemStack[] getArmorContents() {
        ItemStack[] armor = new ItemStack[]{
                getEquipment(EquipmentSlotType.FEET),
                getEquipment(EquipmentSlotType.LEGS),
                getEquipment(EquipmentSlotType.CHEST),
                getEquipment(EquipmentSlotType.HEAD),
        };
        return armor;
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        setEquipment(EquipmentSlotType.FEET, items.length >= 1 ? items[0] : null, false);
        setEquipment(EquipmentSlotType.LEGS, items.length >= 2 ? items[1] : null, false);
        setEquipment(EquipmentSlotType.CHEST, items.length >= 3 ? items[2] : null, false);
        setEquipment(EquipmentSlotType.HEAD, items.length >= 4 ? items[3] : null, false);
    }

    private ItemStack getEquipment(EquipmentSlotType slot) {
        return CraftItemStack.asBukkitCopy(entity.getHandle().getItemBySlot(slot));
    }

    private void setEquipment(EquipmentSlotType slot, ItemStack stack, boolean silent) {
        entity.getHandle().setItemStackToSlot(slot, CraftItemStack.asNMSCopy(stack), silent);
    }

    @Override
    public void clear() {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            setEquipment(slot, null, false);
        }
    }

    @Override
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
       return getDropChance(EquipmentSlotType.MAINHAND);
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        setDropChance(EquipmentSlotType.MAINHAND, chance);
    }

    @Override
    public float getItemInOffHandDropChance() {
        return getDropChance(EquipmentSlotType.OFFHAND);
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        setDropChance(EquipmentSlotType.OFFHAND, chance);
    }

    @Override
    public float getHelmetDropChance() {
        return getDropChance(EquipmentSlotType.HEAD);
    }

    @Override
    public void setHelmetDropChance(float chance) {
        setDropChance(EquipmentSlotType.HEAD, chance);
    }

    @Override
    public float getChestplateDropChance() {
        return getDropChance(EquipmentSlotType.CHEST);
    }

    @Override
    public void setChestplateDropChance(float chance) {
        setDropChance(EquipmentSlotType.CHEST, chance);
    }

    @Override
    public float getLeggingsDropChance() {
        return getDropChance(EquipmentSlotType.LEGS);
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        setDropChance(EquipmentSlotType.LEGS, chance);
    }

    @Override
    public float getBootsDropChance() {
        return getDropChance(EquipmentSlotType.FEET);
    }

    @Override
    public void setBootsDropChance(float chance) {
        setDropChance(EquipmentSlotType.FEET, chance);
    }

    private void setDropChance(EquipmentSlotType slot, float chance) {
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            ((MobEntity) entity.getHandle()).handDropChances[slot.getIndex()] = chance;
        } else {
            ((MobEntity) entity.getHandle()).armorDropChances[slot.getIndex()] = chance;
        }
    }

    private float getDropChance(EquipmentSlotType slot) {
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            return ((MobEntity) entity.getHandle()).handDropChances[slot.getIndex()];
        } else {
            return ((MobEntity) entity.getHandle()).armorDropChances[slot.getIndex()];
        }
    }
}
