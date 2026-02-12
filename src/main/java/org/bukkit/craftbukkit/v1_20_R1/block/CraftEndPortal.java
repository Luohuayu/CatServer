package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import org.bukkit.World;

public class CraftEndPortal extends CraftBlockEntityState<TheEndPortalBlockEntity> {

    public CraftEndPortal(World world, TheEndPortalBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
