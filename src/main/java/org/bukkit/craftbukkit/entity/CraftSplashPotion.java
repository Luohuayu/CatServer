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
import org.bukkit.entity.SplashPotion;

public class CraftSplashPotion extends CraftThrownPotion implements SplashPotion
{
    public CraftSplashPotion(final CraftServer server, final EntityPotion entity) {
        super(server, entity);
    }
    
    @Override
    public void setItem(final ItemStack item) {
        Validate.notNull((Object)item, "ItemStack cannot be null.");
        Validate.isTrue(item.getType() == Material.SPLASH_POTION, "ItemStack must be a splash potion. This item stack was " + item.getType() + ".");
        this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }
    
    @Override
    public EntityPotion getHandle() {
        return (EntityPotion)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftSplashPotion";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.SPLASH_POTION;
    }
}
