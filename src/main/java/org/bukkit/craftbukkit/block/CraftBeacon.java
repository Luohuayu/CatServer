// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import org.bukkit.entity.LivingEntity;
import java.util.Collection;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityBeacon;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Beacon;

public class CraftBeacon extends CraftBlockState implements Beacon
{
    private final CraftWorld world;
    private final TileEntityBeacon beacon;
    
    public CraftBeacon(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.beacon = (TileEntityBeacon)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftBeacon(final Material material, final TileEntityBeacon te) {
        super(material);
        this.world = null;
        this.beacon = te;
    }
    
    @Override
    public Inventory getInventory() {
        return new CraftInventoryBeacon(this.beacon);
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.beacon.markDirty();
        }
        return result;
    }
    
    @Override
    public TileEntityBeacon getTileEntity() {
        return this.beacon;
    }
    
    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        final Collection<EntityPlayer> nms = (Collection<EntityPlayer>)this.beacon.getHumansInRange();
        final Collection<LivingEntity> bukkit = new ArrayList<LivingEntity>(nms.size());
        for (final EntityPlayer human : nms) {
            bukkit.add(human.getBukkitEntity());
        }
        return bukkit;
    }
    
    @Override
    public int getTier() {
        return this.beacon.levels;
    }
    
    @Override
    public PotionEffect getPrimaryEffect() {
        return this.beacon.getPrimaryEffect();
    }
    
    @Override
    public void setPrimaryEffect(final PotionEffectType effect) {
        this.beacon.primaryEffect = ((effect != null) ? Potion.getPotionById(effect.getId()) : null);
    }
    
    @Override
    public PotionEffect getSecondaryEffect() {
        return this.beacon.getSecondaryEffect();
    }
    
    @Override
    public void setSecondaryEffect(final PotionEffectType effect) {
        this.beacon.secondaryEffect = ((effect != null) ? Potion.getPotionById(effect.getId()) : null);
    }
}
