// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.Villager;
import net.minecraft.entity.monster.ZombieType;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie
{
    public CraftZombie(final CraftServer server, final EntityZombie entity) {
        super(server, entity);
    }
    
    @Override
    public EntityZombie getHandle() {
        return (EntityZombie)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftZombie";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE;
    }
    
    @Override
    public boolean isBaby() {
        return this.getHandle().isChild();
    }
    
    @Override
    public void setBaby(final boolean flag) {
        this.getHandle().setChild(flag);
    }
    
    @Override
    public boolean isVillager() {
        return this.getHandle().isVillager();
    }
    
    @Override
    public void setVillager(final boolean flag) {
        this.getHandle().setZombieType(flag ? ZombieType.VILLAGER_FARMER : ZombieType.NORMAL);
    }
    
    @Override
    public void setVillagerProfession(final Villager.Profession profession) {
        this.getHandle().setZombieType((profession == null) ? ZombieType.NORMAL : ZombieType.getByOrdinal(profession.ordinal()));
    }
    
    @Override
    public Villager.Profession getVillagerProfession() {
        return Villager.Profession.values()[this.getHandle().getZombieType().ordinal()];
    }
}
