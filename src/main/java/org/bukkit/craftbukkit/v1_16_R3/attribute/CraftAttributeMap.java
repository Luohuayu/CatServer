package org.bukkit.craftbukkit.v1_16_R3.attribute;

import com.google.common.base.Preconditions;
import moe.loliserver.BukkitInjector;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import org.bukkit.Registry;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;

public class CraftAttributeMap implements Attributable {

    private final AttributeModifierManager handle;

    public CraftAttributeMap(AttributeModifierManager handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.entity.ai.attributes.ModifiableAttributeInstance nms = handle.getInstance(toMinecraft(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    public static net.minecraft.entity.ai.attributes.Attribute toMinecraft(Attribute attribute) {
        net.minecraft.util.ResourceLocation resourceLocation = BukkitInjector.attributemap.get(attribute);
        if (resourceLocation == null) {
            return net.minecraft.util.registry.Registry.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey())); // Minecraft
        } else {
            return net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES.getValue(resourceLocation); // Mod
        }
    }

    public static Attribute fromMinecraft(String nms) {
        Attribute attribute = Registry.ATTRIBUTE.get(CraftNamespacedKey.fromString(nms));
        if (attribute != null) {
            return attribute; // Minecraft
        } else {
            return BukkitInjector.nameToAttributeMap.get(net.minecraft.util.ResourceLocation.tryParse(nms)); // Mod
        }
    }
}
