package catserver.server.utils;

import net.minecraft.tileentity.TileEntityHopper;

public class HopperTask {
    public final TileEntityHopper hopper;
    public final long time;

    public HopperTask(TileEntityHopper hopper, long time) {
        this.hopper = hopper;
        this.time = time;
    }
}
