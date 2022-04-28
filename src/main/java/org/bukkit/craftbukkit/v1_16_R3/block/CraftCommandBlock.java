package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.CommandBlockTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;

public class CraftCommandBlock extends CraftBlockEntityState<CommandBlockTileEntity> implements CommandBlock {

    public CraftCommandBlock(Block block) {
        super(block, CommandBlockTileEntity.class);
    }

    public CraftCommandBlock(final Material material, final CommandBlockTileEntity te) {
        super(material, te);
    }

    @Override
    public String getCommand() {
        return getSnapshot().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(String command) {
        getSnapshot().getCommandBlock().setCommand(command != null ? command : "");
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(getSnapshot().getCommandBlock().getName());
    }

    @Override
    public void setName(String name) {
        getSnapshot().getCommandBlock().setName(CraftChatMessage.fromStringOrNull(name != null ? name : "@"));
    }
}
