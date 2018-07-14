// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.item.Item;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.material.MaterialData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityFlowerPot;
import org.bukkit.block.FlowerPot;

public class CraftFlowerPot extends CraftBlockState implements FlowerPot
{
    private final TileEntityFlowerPot pot;
    
    public CraftFlowerPot(final Block block) {
        super(block);
        this.pot = (TileEntityFlowerPot)((CraftWorld)block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftFlowerPot(final Material material, final TileEntityFlowerPot pot) {
        super(material);
        this.pot = pot;
    }
    
    @Override
    public MaterialData getContents() {
        return (this.pot.getFlowerItemStack() == null) ? null : CraftMagicNumbers.getMaterial(this.pot.getFlowerPotItem()).getNewData((byte)this.pot.getFlowerPotData());
    }
    
    @Override
    public void setContents(final MaterialData item) {
        if (item == null) {
            this.pot.setFlowerPotData(null, 0);
        }
        else {
            this.pot.setFlowerPotData(CraftMagicNumbers.getItem(item.getItemType()), item.getData());
        }
    }
}
