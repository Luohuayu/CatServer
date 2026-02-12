package org.bukkit.craftbukkit.v1_20_R1.entity;

import com.google.common.base.Preconditions;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData;
import org.bukkit.entity.BlockDisplay;

public class CraftBlockDisplay extends CraftDisplay implements BlockDisplay {

    public CraftBlockDisplay(CraftServer server, net.minecraft.world.entity.Display.BlockDisplay entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display.BlockDisplay getHandle() {
        return (net.minecraft.world.entity.Display.BlockDisplay) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftBlockDisplay";
    }

    @Override
    public BlockData getBlock() {
        return CraftBlockData.fromData(getHandle().getBlockState());
    }

    @Override
    public void setBlock(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        getHandle().setBlockState(((CraftBlockData) block).getState());
    }
}
