// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.HumanEntity;
import java.util.Iterator;
import net.minecraft.village.MerchantRecipeList;
import org.bukkit.craftbukkit.inventory.CraftMerchantRecipe;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.inventory.MerchantRecipe;
import java.util.List;
import net.minecraft.inventory.IInventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityVillager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.Villager;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder
{
    public CraftVillager(final CraftServer server, final EntityVillager entity) {
        super(server, entity);
    }
    
    @Override
    public EntityVillager getHandle() {
        return (EntityVillager)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftVillager";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.VILLAGER;
    }
    
    @Override
    public Profession getProfession() {
        return Profession.values()[this.getHandle().getProfession() + 1];
    }
    
    @Override
    public void setProfession(final Profession profession) {
        Validate.notNull((Object)profession);
        Validate.isTrue(!profession.isZombie(), "Profession is reserved for Zombies: ", (Object)profession);
        this.getHandle().setProfession(profession.ordinal() - 1);
    }
    
    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().villagerInventory);
    }
    
    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList((List<? extends MerchantRecipe>)Lists.transform((List)this.getHandle().getRecipes(null), (Function)new Function<net.minecraft.village.MerchantRecipe, MerchantRecipe>() {
            public MerchantRecipe apply(final net.minecraft.village.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }
    
    @Override
    public void setRecipes(final List<MerchantRecipe> list) {
        final MerchantRecipeList recipes = this.getHandle().getRecipes(null);
        recipes.clear();
        for (final MerchantRecipe m : list) {
            recipes.add(CraftMerchantRecipe.fromBukkit(m).toMinecraft());
        }
    }
    
    @Override
    public MerchantRecipe getRecipe(final int i) {
        return this.getHandle().getRecipes(null).get(i).asBukkit();
    }
    
    @Override
    public void setRecipe(final int i, final MerchantRecipe merchantRecipe) {
        this.getHandle().getRecipes(null).set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }
    
    @Override
    public int getRecipeCount() {
        return this.getHandle().getRecipes(null).size();
    }
    
    @Override
    public boolean isTrading() {
        return this.getTrader() != null;
    }
    
    @Override
    public HumanEntity getTrader() {
        final EntityPlayer eh = this.getHandle().getCustomer();
        return (eh == null) ? null : eh.getBukkitEntity();
    }
    
    @Override
    public int getRiches() {
        return this.getHandle().wealth;
    }
    
    @Override
    public void setRiches(final int riches) {
        this.getHandle().wealth = riches;
    }
}
