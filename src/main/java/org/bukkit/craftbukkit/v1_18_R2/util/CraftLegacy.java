package org.bukkit.craftbukkit.v1_18_R2.util;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * @deprecated legacy use only
 */
@Deprecated
// https://github.com/IzzelAliz/Arclight/blob/1.18/arclight-common/src/main/java/io/izzel/arclight/common/mixin/bukkit/CraftLegacyUtilMixin.java
public final class CraftLegacy {

    private static Material[] moddedMaterials; // CatServer
    private static int offset; // CatServer

    private CraftLegacy() {
        //
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return org.bukkit.craftbukkit.v1_18_R2.legacy.CraftLegacy.fromLegacy(material);
    }

    public static Material fromLegacy(MaterialData materialData) {
        return org.bukkit.craftbukkit.v1_18_R2.legacy.CraftLegacy.fromLegacy(materialData);
    }

    public static Material[] modern_values() {
        if (moddedMaterials == null) {
            int origin = Material.values().length;
            moddedMaterials = Arrays.stream(Material.values()).filter(it -> !it.isLegacy()).toArray(Material[]::new);
            offset = origin - moddedMaterials.length;
        }
        return Arrays.copyOf(moddedMaterials, moddedMaterials.length);
    }

    public static int modern_ordinal(Material material) {
        if (moddedMaterials == null) {
            modern_values();
        }
        if (material.isLegacy()) {
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        } else {
            int ordinal = material.ordinal();
            return ordinal < Material.LEGACY_AIR.ordinal() ? ordinal : ordinal - offset;
        }
    }
}
