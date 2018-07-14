// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.attribute;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import com.google.common.base.Preconditions;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.Attribute;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import org.bukkit.attribute.Attributable;

public class CraftAttributeMap implements Attributable
{
    private final AbstractAttributeMap handle;
    
    public CraftAttributeMap(final AbstractAttributeMap handle) {
        this.handle = handle;
    }
    
    @Override
    public AttributeInstance getAttribute(final Attribute attribute) {
        Preconditions.checkArgument(attribute != null, (Object)"attribute");
        final IAttributeInstance nms = this.handle.getAttributeInstanceByName(toMinecraft(attribute.name()));
        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }
    
    static String toMinecraft(final String bukkit) {
        final int first = bukkit.indexOf(95);
        final int second = bukkit.indexOf(95, first + 1);
        final StringBuilder sb = new StringBuilder(bukkit.toLowerCase(Locale.ENGLISH));
        sb.setCharAt(first, '.');
        if (second != -1) {
            sb.deleteCharAt(second);
            sb.setCharAt(second, bukkit.charAt(second + 1));
        }
        return sb.toString();
    }
}
