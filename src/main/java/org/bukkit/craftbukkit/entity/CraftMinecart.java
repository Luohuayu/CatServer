// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;

public class CraftMinecart extends CraftVehicle implements Minecart
{
    public CraftMinecart(final CraftServer server, final EntityMinecart entity) {
        super(server, entity);
    }
    
    @Override
    public void setDamage(final double damage) {
        this.getHandle().setDamage((float)damage);
    }
    
    @Override
    public double getDamage() {
        return this.getHandle().getDamage();
    }
    
    @Override
    public double getMaxSpeed() {
        return this.getHandle().maxSpeed;
    }
    
    @Override
    public void setMaxSpeed(final double speed) {
        if (speed >= 0.0) {
            this.getHandle().maxSpeed = speed;
        }
    }
    
    @Override
    public boolean isSlowWhenEmpty() {
        return this.getHandle().slowWhenEmpty;
    }
    
    @Override
    public void setSlowWhenEmpty(final boolean slow) {
        this.getHandle().slowWhenEmpty = slow;
    }
    
    @Override
    public Vector getFlyingVelocityMod() {
        return this.getHandle().getFlyingVelocityMod();
    }
    
    @Override
    public void setFlyingVelocityMod(final Vector flying) {
        this.getHandle().setFlyingVelocityMod(flying);
    }
    
    @Override
    public Vector getDerailedVelocityMod() {
        return this.getHandle().getDerailedVelocityMod();
    }
    
    @Override
    public void setDerailedVelocityMod(final Vector derailed) {
        this.getHandle().setDerailedVelocityMod(derailed);
    }
    
    @Override
    public EntityMinecart getHandle() {
        return (EntityMinecart)this.entity;
    }
    
    @Deprecated
    @Override
    public void _INVALID_setDamage(final int damage) {
        this.setDamage(damage);
    }
    
    @Deprecated
    @Override
    public int _INVALID_getDamage() {
        return NumberConversions.ceil(this.getDamage());
    }
    
    @Override
    public void setDisplayBlock(final MaterialData material) {
        if (material != null) {
            final IBlockState block = CraftMagicNumbers.getBlock(material.getItemTypeId()).getStateFromMeta(material.getData());
            this.getHandle().setDisplayTile(block);
        }
        else {
            this.getHandle().setDisplayTile(Blocks.AIR.getDefaultState());
            this.getHandle().setHasDisplayTile(false);
        }
    }
    
    @Override
    public MaterialData getDisplayBlock() {
        final IBlockState blockData = this.getHandle().getDisplayTile();
        return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte)blockData.getBlock().getMetaFromState(blockData));
    }
    
    @Override
    public void setDisplayBlockOffset(final int offset) {
        this.getHandle().setDisplayTileOffset(offset);
    }
    
    @Override
    public int getDisplayBlockOffset() {
        return this.getHandle().getDisplayTileOffset();
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART;
    }
}
