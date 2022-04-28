package org.bukkit.craftbukkit.v1_16_R3.block;

import net.minecraft.tileentity.DaylightDetectorTileEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<DaylightDetectorTileEntity> implements DaylightDetector {

    public CraftDaylightDetector(final Block block) {
        super(block, DaylightDetectorTileEntity.class);
    }

    public CraftDaylightDetector(final Material material, final DaylightDetectorTileEntity te) {
        super(material, te);
    }
}
