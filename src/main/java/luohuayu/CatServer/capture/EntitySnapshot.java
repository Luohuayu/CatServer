package luohuayu.CatServer.capture;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntitySnapshot{

    public final World mWorld;
    public final Entity mEntity;
    public final SpawnReason mReason;

    private boolean mApply=false;

    public EntitySnapshot(World pWorld,Entity pEntity,SpawnReason pReason){
        this.mWorld=pWorld;
        this.mEntity=pEntity;
        this.mReason=pReason==null?SpawnReason.CUSTOM:pReason;
    }

    public boolean apply(){
        if(!this.mApply){
            this.mApply=true;
            return this.mWorld.addEntity(this.mEntity,this.mReason);
        }
        return true;
    }

    public void cancel(){
        this.mEntity.setDead();
        this.mWorld.removeEntity(this.mEntity);
    }
}
