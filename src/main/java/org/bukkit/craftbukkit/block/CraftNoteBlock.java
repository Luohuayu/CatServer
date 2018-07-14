// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.Instrument;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Note;
import org.bukkit.Material;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityNote;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.NoteBlock;

public class CraftNoteBlock extends CraftBlockState implements NoteBlock
{
    private final CraftWorld world;
    private final TileEntityNote note;
    
    public CraftNoteBlock(final Block block) {
        super(block);
        this.world = (CraftWorld)block.getWorld();
        this.note = (TileEntityNote)this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftNoteBlock(final Material material, final TileEntityNote te) {
        super(material);
        this.world = null;
        this.note = te;
    }
    
    @Override
    public Note getNote() {
        return new Note(this.note.note);
    }
    
    @Override
    public byte getRawNote() {
        return this.note.note;
    }
    
    @Override
    public void setNote(final Note n) {
        this.note.note = n.getId();
    }
    
    @Override
    public void setRawNote(final byte n) {
        this.note.note = n;
    }
    
    @Override
    public boolean play() {
        final Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            this.note.triggerNote(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean play(final byte instrument, final byte note) {
        final Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            this.world.getHandle().addBlockEvent(new BlockPos(this.getX(), this.getY(), this.getZ()), CraftMagicNumbers.getBlock(block), instrument, note);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean play(final Instrument instrument, final Note note) {
        final Block block = this.getBlock();
        if (block.getType() == Material.NOTE_BLOCK) {
            this.world.getHandle().addBlockEvent(new BlockPos(this.getX(), this.getY(), this.getZ()), CraftMagicNumbers.getBlock(block), instrument.getType(), note.getId());
            return true;
        }
        return false;
    }
    
    @Override
    public TileEntityNote getTileEntity() {
        return this.note;
    }
}
