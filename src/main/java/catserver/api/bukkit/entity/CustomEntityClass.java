package catserver.api.bukkit.entity;

import javax.annotation.Nullable;

import catserver.server.utils.NMSUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CustomEntityClass {
    private final String entityName;
    private final Class<? extends Entity> entityClass;

    public CustomEntityClass(String entityName, Class<? extends Entity> entityClass) {
        this.entityName = entityName;
        this.entityClass = entityClass;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public Class<? extends Entity> getEntityClass() {
        return this.entityClass;
    }

    @Nullable
    public Entity newInstance(WorldServer world) {
        Entity entity = null;
        try {
            entity = entityClass.getConstructor(World.class).newInstance(world);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Nullable
    public Entity newInstance(org.bukkit.World world) {
        return newInstance(NMSUtils.toNMS(world));
    }

    @Nullable
    public org.bukkit.entity.Entity spawn(org.bukkit.World world, double x, double y, double z) {
        WorldServer worldserver = NMSUtils.toNMS(world);
        Entity entity = newInstance(worldserver);
        if (entity == null) return null;
        entity.setPosition(x, y, z);
        worldserver.spawnEntity(entity);
        return entity.getBukkitEntity();
    }

    public boolean isLivingbase() {
        return EntityLivingBase.class.isAssignableFrom(this.entityClass);
    }
    
    public boolean isCreature() {
        return EntityCreature.class.isAssignableFrom(this.entityClass);
    }

    public boolean isAnimal() {
        return EntityAnimal.class.isAssignableFrom(this.entityClass);
    }

    public boolean isMonster() {
        return EntityMob.class.isAssignableFrom(this.entityClass);
    }
}
