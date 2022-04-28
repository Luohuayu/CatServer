package org.bukkit.craftbukkit.v1_16_R3.util;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * @deprecated legacy use only
 */
@Deprecated
public final class CraftLegacy {

    private CraftLegacy() {
        //
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return org.bukkit.craftbukkit.v1_16_R3.legacy.CraftLegacy.fromLegacy(material);
    }

    public static Material fromLegacy(MaterialData materialData) {
        return org.bukkit.craftbukkit.v1_16_R3.legacy.CraftLegacy.fromLegacy(materialData);
    }

    public static Material[] modern_values() {
        Material[] materials = Material.values();
        Material[] modernWithForge = new Material[materials.length - (CraftMagicNumbers.LEGACY_LAST_POS - CraftMagicNumbers.LEGACY_FIRST_POS + 1)];
        System.arraycopy(materials, 0, modernWithForge, 0, CraftMagicNumbers.LEGACY_FIRST_POS);
        System.arraycopy(materials, CraftMagicNumbers.LEGACY_LAST_POS + 1, modernWithForge, CraftMagicNumbers.LEGACY_FIRST_POS, materials.length - CraftMagicNumbers.LEGACY_LAST_POS - 1);
        return modernWithForge;
    }

    public static int modern_ordinal(Material material) {
        if (material.isLegacy()) {
            // SPIGOT-4002: Fix for eclipse compiler manually compiling in default statements to lookupswitch
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        }

        int ord = material.ordinal();
        if (ord > CraftMagicNumbers.LEGACY_LAST_POS) {
            return CraftMagicNumbers.LEGACY_FIRST_POS + ord - CraftMagicNumbers.LEGACY_LAST_POS - 1;
        }
        else {
            return ord;
        }
    }
}
