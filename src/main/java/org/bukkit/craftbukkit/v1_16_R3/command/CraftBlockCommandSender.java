package org.bukkit.craftbukkit.v1_16_R3.command;

import net.minecraft.command.CommandSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

/**
 * Represents input from a command block
 */
public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {
    private final CommandSource block;
    private final TileEntity tile;

    public CraftBlockCommandSender(CommandSource commandBlockListenerAbstract, TileEntity tile) {
        super();
        this.block = commandBlockListenerAbstract;
        this.tile = tile;
    }

    @Override
    public Block getBlock() {
        return CraftBlock.at(tile.getLevel(), tile.getBlockPos());
    }

    @Override
    public void sendMessage(String message) {
        for (ITextComponent component : CraftChatMessage.fromString(message)) {
            block.source.sendMessage(component, Util.NIL_UUID);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return block.getTextName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public CommandSource getWrapper() {
        return block;
    }
}
