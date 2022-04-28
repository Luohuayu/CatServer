package org.bukkit.craftbukkit.v1_16_R3;

import net.minecraft.inventory.EquipmentSlotType;
import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final EquipmentSlotType[] slots = new EquipmentSlotType[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[EquipmentSlotType.values().length];

    static {
        set(EquipmentSlot.HAND, EquipmentSlotType.MAINHAND);
        set(EquipmentSlot.OFF_HAND, EquipmentSlotType.OFFHAND);
        set(EquipmentSlot.FEET, EquipmentSlotType.FEET);
        set(EquipmentSlot.LEGS, EquipmentSlotType.LEGS);
        set(EquipmentSlot.CHEST, EquipmentSlotType.CHEST);
        set(EquipmentSlot.HEAD, EquipmentSlotType.HEAD);
    }

    private static void set(EquipmentSlot type, EquipmentSlotType value) {
        slots[type.ordinal()] = value;
        enums[value.ordinal()] = type;
    }

    public static EquipmentSlot getSlot(EquipmentSlotType nms) {
        return enums[nms.ordinal()];
    }

    public static EquipmentSlotType getNMS(EquipmentSlot slot) {
        return slots[slot.ordinal()];
    }
}
