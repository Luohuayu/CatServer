// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.apache.commons.lang.Validate;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityOcelot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ocelot;

public class CraftOcelot extends CraftTameableAnimal implements Ocelot
{
    public CraftOcelot(final CraftServer server, final EntityOcelot wolf) {
        super(server, wolf);
    }
    
    @Override
    public EntityOcelot getHandle() {
        return (EntityOcelot)this.entity;
    }
    
    @Override
    public Type getCatType() {
        return Type.getType(this.getHandle().getTameSkin());
    }
    
    @Override
    public void setCatType(final Type type) {
        Validate.notNull((Object)type, "Cat type cannot be null");
        this.getHandle().setTameSkin(type.getId());
    }
    
    @Override
    public EntityType getType() {
        return EntityType.OCELOT;
    }
}
