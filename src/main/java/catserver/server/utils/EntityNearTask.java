package catserver.server.utils;

import net.minecraft.entity.EntityLivingBase;

public class EntityNearTask implements Runnable {
    public final EntityLivingBase entity;
    public final long time;

    public EntityNearTask(EntityLivingBase entity, long time) {
        this.entity = entity;
        this.time = time;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - this.time > 100)
            return;
        if (this.entity.world.unloadedEntitySet.contains(this.entity))
            return;
        // Start
        this.entity.collideWithNearbyEntities0();
    }
}
