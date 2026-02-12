package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import org.bukkit.World;
import org.bukkit.block.CalibratedSculkSensor;

public class CraftCalibratedSculkSensor extends CraftSculkSensor<CalibratedSculkSensorBlockEntity> implements CalibratedSculkSensor {

    public CraftCalibratedSculkSensor(World world, CalibratedSculkSensorBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
