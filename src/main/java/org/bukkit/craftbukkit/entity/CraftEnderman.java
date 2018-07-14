// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.EntityType;
import net.minecraft.block.state.IBlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityEnderman;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Enderman;

public class CraftEnderman extends CraftMonster implements Enderman
{
    public CraftEnderman(final CraftServer server, final EntityEnderman entity) {
        super(server, entity);
    }
    
    @Override
    public MaterialData getCarriedMaterial() {
        final IBlockState blockData = this.getHandle().getHeldBlockState();
        return (blockData == null) ? Material.AIR.getNewData((byte)0) : CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte)blockData.getBlock().getMetaFromState(blockData));
    }
    
    @Override
    public void setCarriedMaterial(final MaterialData data) {
        this.getHandle().setHeldBlockState(CraftMagicNumbers.getBlock(data.getItemTypeId()).getStateFromMeta(data.getData()));
    }
    
    @Override
    public EntityEnderman getHandle() {
        return (EntityEnderman)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftEnderman";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}
