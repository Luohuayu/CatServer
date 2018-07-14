// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.command;

import net.minecraft.util.text.ITextComponent;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.block.Block;
import net.minecraft.command.ICommandSender;
import org.bukkit.command.BlockCommandSender;

public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender
{
    private final ICommandSender block;
    
    public CraftBlockCommandSender(final ICommandSender commandBlockListenerAbstract) {
        this.block = commandBlockListenerAbstract;
    }
    
    @Override
    public Block getBlock() {
        return this.block.getEntityWorld().getWorld().getBlockAt(this.block.getPosition().getX(), this.block.getPosition().getY(), this.block.getPosition().getZ());
    }
    
    @Override
    public void sendMessage(final String message) {
        ITextComponent[] fromString;
        for (int length = (fromString = CraftChatMessage.fromString(message)).length, i = 0; i < length; ++i) {
            final ITextComponent component = fromString[i];
            this.block.addChatMessage(component);
        }
    }
    
    @Override
    public void sendMessage(final String[] messages) {
        for (final String message : messages) {
            this.sendMessage(message);
        }
    }
    
    @Override
    public String getName() {
        return this.block.getName();
    }
    
    @Override
    public boolean isOp() {
        return true;
    }
    
    @Override
    public void setOp(final boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }
    
    public ICommandSender getTileEntity() {
        return this.block;
    }
}
