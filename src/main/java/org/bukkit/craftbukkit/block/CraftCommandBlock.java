// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityCommandBlock;
import org.bukkit.block.CommandBlock;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock
{
    private final TileEntityCommandBlock commandBlock;
    private String command;
    private String name;
    
    public CraftCommandBlock(final Block block) {
        super(block);
        final CraftWorld world = (CraftWorld)block.getWorld();
        this.commandBlock = (TileEntityCommandBlock)world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.command = this.commandBlock.getCommandBlockLogic().getCommand();
        this.name = this.commandBlock.getCommandBlockLogic().getName();
    }
    
    public CraftCommandBlock(final Material material, final TileEntityCommandBlock te) {
        super(material);
        this.commandBlock = te;
        this.command = this.commandBlock.getCommandBlockLogic().getCommand();
        this.name = this.commandBlock.getCommandBlockLogic().getName();
    }
    
    @Override
    public String getCommand() {
        return this.command;
    }
    
    @Override
    public void setCommand(final String command) {
        this.command = ((command != null) ? command : "");
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(final String name) {
        this.name = ((name != null) ? name : "@");
    }
    
    @Override
    public boolean update(final boolean force, final boolean applyPhysics) {
        final boolean result = super.update(force, applyPhysics);
        if (result) {
            this.commandBlock.getCommandBlockLogic().setCommand(this.command);
            this.commandBlock.getCommandBlockLogic().setName(this.name);
        }
        return result;
    }
    
    @Override
    public TileEntityCommandBlock getTileEntity() {
        return this.commandBlock;
    }
}
