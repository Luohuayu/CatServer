package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.World;
import org.bukkit.block.Smoker;

public class CraftSmoker extends CraftFurnace<SmokerBlockEntity> implements Smoker {

    public CraftSmoker(World world, SmokerBlockEntity tileEntity) {
        super(world, tileEntity);
    }
}
