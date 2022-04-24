package org.bukkit.craftbukkit.v1_18_R2;

import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final net.minecraft.world.entity.EquipmentSlot[] slots = new net.minecraft.world.entity.EquipmentSlot[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[net.minecraft.world.entity.EquipmentSlot.values().length];

    static {
        set(EquipmentSlot.HAND, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        set(EquipmentSlot.OFF_HAND, net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        set(EquipmentSlot.FEET, net.minecraft.world.entity.EquipmentSlot.FEET);
        set(EquipmentSlot.LEGS, net.minecraft.world.entity.EquipmentSlot.LEGS);
        set(EquipmentSlot.CHEST, net.minecraft.world.entity.EquipmentSlot.CHEST);
        set(EquipmentSlot.HEAD, net.minecraft.world.entity.EquipmentSlot.HEAD);
    }

    private static void set(EquipmentSlot type, net.minecraft.world.entity.EquipmentSlot value) {
        slots[type.ordinal()] = value;
        enums[value.ordinal()] = type;
    }

    public static EquipmentSlot getSlot(net.minecraft.world.entity.EquipmentSlot nms) {
        return enums[nms.ordinal()];
    }

    public static net.minecraft.world.entity.EquipmentSlot getNMS(EquipmentSlot slot) {
        return slots[slot.ordinal()];
    }
}
