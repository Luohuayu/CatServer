// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import net.minecraft.init.Items;
import com.google.common.base.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import java.util.Random;
import org.bukkit.entity.Firework;

public class CraftFirework extends CraftEntity implements Firework
{
    private final Random random;
    private final CraftItemStack item;
    
    public CraftFirework(final CraftServer server, final EntityFireworkRocket entity) {
        super(server, entity);
        this.random = new Random();
        ItemStack item = (ItemStack)this.getHandle().getDataManager().get(EntityFireworkRocket.FIREWORK_ITEM).orNull();
        if (item == null) {
            item = new ItemStack(Items.FIREWORKS);
            this.getHandle().getDataManager().set(EntityFireworkRocket.FIREWORK_ITEM, /*(Optional<ItemStack>)*/Optional.of(/*(Object)*/item));
        }
        this.item = CraftItemStack.asCraftMirror(item);
        if (this.item.getType() != Material.FIREWORK) {
            this.item.setType(Material.FIREWORK);
        }
    }
    
    @Override
    public EntityFireworkRocket getHandle() {
        return (EntityFireworkRocket)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftFirework";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }
    
    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta)this.item.getItemMeta();
    }
    
    @Override
    public void setFireworkMeta(final FireworkMeta meta) {
        this.item.setItemMeta(meta);
        this.getHandle().lifetime = 10 * (1 + meta.getPower()) + this.random.nextInt(6) + this.random.nextInt(7);
        this.getHandle().getDataManager().setDirty(EntityFireworkRocket.FIREWORK_ITEM);
    }
    
    @Override
    public void detonate() {
        this.getHandle().lifetime = 0;
    }
}
