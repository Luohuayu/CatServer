package org.bukkit.craftbukkit.v1_20_R1.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;

public class CraftAttributeMap implements Attributable {

    private final AttributeMap handle;

    public CraftAttributeMap(AttributeMap handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.world.entity.ai.attributes.AttributeInstance nms = handle.getInstance(toMinecraft(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    public static net.minecraft.world.entity.ai.attributes.Attribute toMinecraft(Attribute attribute) {
        // CatServer start
        net.minecraft.resources.ResourceLocation resourceLocation = catserver.server.BukkitInjector.attributeToNameMap.get(attribute);
        if (resourceLocation == null) {
            return BuiltInRegistries.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey())); // Minecraft
        } else {
            return net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES.getValue(resourceLocation); // Mod
        }
        // CatServer end
    }

    public static Attribute fromMinecraft(String nms) {
        // CatServer start
        Attribute attribute = Registry.ATTRIBUTE.get(CraftNamespacedKey.fromString(nms));
        if (attribute != null) {
            return attribute; // Minecraft
        } else {
            return catserver.server.BukkitInjector.nameToAttributeMap.get(net.minecraft.resources.ResourceLocation.tryParse(nms)); // Mod
        }
        // CatServer end
    }
}
