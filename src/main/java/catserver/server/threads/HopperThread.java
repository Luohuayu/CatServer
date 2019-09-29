package catserver.server.threads;

import catserver.server.utils.HopperTask;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.WorldServer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HopperThread extends Thread {

    private final WorldServer world;
    private final LinkedBlockingQueue<HopperTask> queue;

    public HopperThread(WorldServer worldServer, LinkedBlockingQueue<HopperTask> queue) {
        this.world = worldServer;
        this.queue = queue;
    }

    @Override
    public void run() {
        long nowTime = System.currentTimeMillis();
        short count = 0;
        while (world != null) {
            try{
                if ((count++ % 10) == 0) {
                    nowTime = System.currentTimeMillis();
                    count = 0;
                }
                HopperTask hopperTask = queue.take();
                TileEntityHopper hopper = hopperTask.hopper;
                if (nowTime - hopperTask.time > 200) continue;
                if (!world.isBlockLoaded(hopper.getPos())) continue;
                if (!this.world.isRemote)
                {
                    --hopper.transferCooldown;
                    hopper.tickedGameTime = this.world.getTotalWorldTime();

                    if (!hopper.isOnTransferCooldown())
                    {
                        hopper.setTransferCooldown(0);
                        hopper.lock.lock();
                        try {
                            hopper.updateHopper();
                        }finally {
                            hopper.lock.unlock();
                        }
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
