package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.JigsawTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jigsaw;

public class CraftJigsaw extends CraftBlockEntityState<JigsawTileEntity> implements Jigsaw {

    public CraftJigsaw(Block block) {
        super(block, JigsawTileEntity.class);
    }

    public CraftJigsaw(Material material, JigsawTileEntity te) {
        super(material, te);
    }
}
