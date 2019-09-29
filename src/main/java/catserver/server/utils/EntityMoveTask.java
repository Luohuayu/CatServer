package catserver.server.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;

public class EntityMoveTask implements Runnable {
    public final Entity entity;
    public final MoverType moverType;
    public final double x;
    public final double y;
    public final double z;
    public final long time;

    public EntityMoveTask(Entity entity, MoverType moverType, double x, double y, double z, long time) {
        this.entity = entity;
        this.moverType = moverType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            if (System.currentTimeMillis() - this.time > 100)
                return;
            if (this.entity.world.unloadedEntitySet.contains(this.entity))
                return;
            //Start
            this.entity.move0(this.moverType, this.x, this.y, this.z, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
