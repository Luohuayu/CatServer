package org.bukkit.craftbukkit.v1_16_R3.attribute;

import com.google.common.base.Preconditions;
import moe.loliserver.BukkitInjector;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraftforge.registries.ForgeRegistries;
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
        return !BukkitInjector.attributemap.containsKey(attribute) ? net.minecraft.util.registry.Registry.ATTRIBUTE.get(CraftNamespacedKey.toMinecraft(attribute.getKey())) : ForgeRegistries.ATTRIBUTES.getValue(BukkitInjector.attributemap.get(attribute));
    }

    public static Attribute fromMinecraft(String nms) {
        return Registry.ATTRIBUTE.get(CraftNamespacedKey.fromString(nms));
    }
}
