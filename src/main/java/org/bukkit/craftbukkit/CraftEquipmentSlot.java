// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.inventory.EquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot;

public class CraftEquipmentSlot
{
    private static final EntityEquipmentSlot[] slots;
    private static final EquipmentSlot[] enums;
    
    static {
        slots = new EntityEquipmentSlot[EquipmentSlot.values().length];
        enums = new EquipmentSlot[EntityEquipmentSlot.values().length];
        set(EquipmentSlot.HAND, EntityEquipmentSlot.MAINHAND);
        set(EquipmentSlot.OFF_HAND, EntityEquipmentSlot.OFFHAND);
        set(EquipmentSlot.FEET, EntityEquipmentSlot.FEET);
        set(EquipmentSlot.LEGS, EntityEquipmentSlot.LEGS);
        set(EquipmentSlot.CHEST, EntityEquipmentSlot.CHEST);
        set(EquipmentSlot.HEAD, EntityEquipmentSlot.HEAD);
    }
    
    private static void set(final EquipmentSlot type, final EntityEquipmentSlot value) {
        CraftEquipmentSlot.slots[type.ordinal()] = value;
        CraftEquipmentSlot.enums[value.ordinal()] = type;
    }
    
    public static EquipmentSlot getSlot(final EntityEquipmentSlot nms) {
        return CraftEquipmentSlot.enums[nms.ordinal()];
    }
    
    public static EntityEquipmentSlot getNMS(final EquipmentSlot slot) {
        return CraftEquipmentSlot.slots[slot.ordinal()];
    }
}
