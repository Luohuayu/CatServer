package org.bukkit.craftbukkit;

import net.minecraft.inventory.EntityEquipmentSlot;
import org.bukkit.inventory.EquipmentSlot;

public class CraftEquipmentSlot {

    private static final EntityEquipmentSlot[] slots = new EntityEquipmentSlot[EquipmentSlot.values().length];
    private static final EquipmentSlot[] enums = new EquipmentSlot[EntityEquipmentSlot.values().length];

    static {
        set(EquipmentSlot.HAND, EntityEquipmentSlot.MAINHAND);
        set(EquipmentSlot.OFF_HAND, EntityEquipmentSlot.OFFHAND);
        set(EquipmentSlot.FEET, EntityEquipmentSlot.FEET);
        set(EquipmentSlot.LEGS, EntityEquipmentSlot.LEGS);
        set(EquipmentSlot.CHEST, EntityEquipmentSlot.CHEST);
        set(EquipmentSlot.HEAD, EntityEquipmentSlot.HEAD);
    }

    private static void set(EquipmentSlot type, EntityEquipmentSlot value) {
        slots[type.ordinal()] = value;
        enums[value.ordinal()] = type;
    }

    public static EquipmentSlot getSlot(EntityEquipmentSlot nms) {
        return enums[nms.ordinal()];
    }

    public static EntityEquipmentSlot getNMS(EquipmentSlot slot) {
        return slots[slot.ordinal()];
    }
}
