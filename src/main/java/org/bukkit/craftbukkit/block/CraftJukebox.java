// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.bukkit.Effect;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.block.BlockJukebox;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Jukebox;

public class CraftJukebox extends CraftBlockState implements Jukebox
{
    private final CraftWorld world;
    private final BlockJukebox.TileEntityJukebox jukebox;
    
    public CraftJukebox(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.jukebox = (BlockJukebox.TileEntityJukebox)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftJukebox(final Material material, final BlockJukebox.TileEntityJukebox te) {
        super(material);
        this.world = null;
        this.jukebox = te;
    }
    
    @Override
    public Material getPlaying() {
        final ItemStack record = this.jukebox.getRecord();
        if (record == null) {
            return Material.AIR;
        }
        return CraftMagicNumbers.getMaterial(record.getItem());
    }
    
    @Override
    public void setPlaying(Material record) {
        if (record == null || CraftMagicNumbers.getItem(record) == null) {
            record = Material.AIR;
            this.jukebox.setRecord(null);
        }
        else {
            this.jukebox.setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        }
        if (!this.isPlaced()) {
            return;
        }
        this.jukebox.markDirty();
        if (record == Material.AIR) {
            this.world.getHandle().setBlockState(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.JUKEBOX.getDefaultState().withProperty(/*(IProperty<Comparable>)*/BlockJukebox.HAS_RECORD, false), 3);
        }
        else {
            this.world.getHandle().setBlockState(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.JUKEBOX.getDefaultState().withProperty(/*(IProperty<Comparable>)*/BlockJukebox.HAS_RECORD, true), 3);
        }
        this.world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
    }
    
    @Override
    public boolean isPlaying() {
        return this.getRawData() == 1;
    }
    
    @Override
    public boolean eject() {
        this.requirePlaced();
        final boolean result = this.isPlaying();
        ((BlockJukebox)Blocks.JUKEBOX).dropRecord(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()), null);
        return result;
    }
    
    @Override
    public BlockJukebox.TileEntityJukebox getTileEntity() {
        return this.jukebox;
    }
}
