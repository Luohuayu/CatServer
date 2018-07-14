// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.Material;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import net.minecraft.entity.projectile.EntityPotion;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LingeringPotion;

public class CraftLingeringPotion extends CraftThrownPotion implements LingeringPotion
{
    public CraftLingeringPotion(final CraftServer server, final EntityPotion entity) {
        super(server, entity);
    }
    
    @Override
    public void setItem(final ItemStack item) {
        Validate.notNull((Object)item, "ItemStack cannot be null.");
        Validate.isTrue(item.getType() == Material.LINGERING_POTION, "ItemStack must be a lingering potion. This item stack was " + item.getType() + ".");
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }
    
    @Override
    public EntityPotion getHandle() {
        return (EntityPotion)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftLingeringPotion";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.LINGERING_POTION;
    }
}
