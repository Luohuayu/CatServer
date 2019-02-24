package org.bukkit.craftbukkit.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final ICommandSender block;

    public CraftBlockCommandSender(ICommandSender commandBlockListenerAbstract) {
        super();
        this.block = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return block.getEntityWorld().getWorld().getBlockAt(block.getPosition().getX(), block.getPosition().getY(), block.getPosition().getZ());
    }

    public void sendMessage(String message) {
        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            block.sendMessage(component);
        }
    }

    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    public String getName() {
        return block.getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public ICommandSender getTileEntity() {
        return block;
    }
}
