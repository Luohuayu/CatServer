package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class CraftAttributeInstance implements AttributeInstance {

    private final net.minecraft.entity.ai.attributes.IAttributeInstance handle;
    private final Attribute attribute;

    public CraftAttributeInstance(net.minecraft.entity.ai.attributes.IAttributeInstance handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public double getBaseValue() {
        return handle.getBaseValue();
    }

    @Override
    public void setBaseValue(double d) {
        handle.setBaseValue(d);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (net.minecraft.entity.ai.attributes.AttributeModifier nms : handle.getModifiers()) {
            result.add(convert(nms));
        }

        return result;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.applyModifier(convert(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.removeModifier(convert(modifier));
    }

    @Override
    public double getValue() {
        return handle.getAttributeValue();
    }

    @Override
    public double getDefaultValue() {
       return handle.getAttribute().getDefaultValue();
    }

    private static net.minecraft.entity.ai.attributes.AttributeModifier convert(AttributeModifier bukkit) {
        return new net.minecraft.entity.ai.attributes.AttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), bukkit.getOperation().ordinal());
    }

    private static AttributeModifier convert(net.minecraft.entity.ai.attributes.AttributeModifier nms) {
        return new AttributeModifier(nms.getID(), nms.getName(), nms.getAmount(), AttributeModifier.Operation.values()[nms.getOperation()]);
    }
}
