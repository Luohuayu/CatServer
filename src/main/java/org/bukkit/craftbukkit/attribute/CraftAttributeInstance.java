// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.attribute.AttributeModifier;
import java.util.Collection;
import org.bukkit.attribute.Attribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import org.bukkit.attribute.AttributeInstance;

public class CraftAttributeInstance implements AttributeInstance
{
    private final IAttributeInstance handle;
    private final Attribute attribute;
    
    public CraftAttributeInstance(final IAttributeInstance handle, final Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }
    
    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }
    
    @Override
    public double getBaseValue() {
        return this.handle.getBaseValue();
    }
    
    @Override
    public void setBaseValue(final double d) {
        this.handle.setBaseValue(d);
    }
    
    @Override
    public Collection<AttributeModifier> getModifiers() {
        final List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (final net.minecraft.entity.ai.attributes.AttributeModifier nms : this.handle.getModifiers()) {
            result.add(convert(nms));
        }
        return result;
    }
    
    @Override
    public void addModifier(final AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, (Object)"modifier");
        this.handle.applyModifier(convert(modifier));
    }
    
    @Override
    public void removeModifier(final AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, (Object)"modifier");
        this.handle.removeModifier(convert(modifier));
    }
    
    @Override
    public double getValue() {
        return this.handle.getAttributeValue();
    }
    
    private static net.minecraft.entity.ai.attributes.AttributeModifier convert(final AttributeModifier bukkit) {
        return new net.minecraft.entity.ai.attributes.AttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), bukkit.getOperation().ordinal());
    }
    
    private static AttributeModifier convert(final net.minecraft.entity.ai.attributes.AttributeModifier nms) {
        return new AttributeModifier(nms.getID(), nms.getName(), nms.getAmount(), AttributeModifier.Operation.values()[nms.getOperation()]);
    }
}
