// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityAgeable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ageable;

public class CraftAgeable extends CraftCreature implements Ageable
{
    public CraftAgeable(final CraftServer server, final EntityAgeable entity) {
        super(server, entity);
    }
    
    @Override
    public int getAge() {
        return this.getHandle().getGrowingAge();
    }
    
    @Override
    public void setAge(final int age) {
        this.getHandle().setGrowingAge(age);
    }
    
    @Override
    public void setAgeLock(final boolean lock) {
        this.getHandle().ageLocked = lock;
    }
    
    @Override
    public boolean getAgeLock() {
        return this.getHandle().ageLocked;
    }
    
    @Override
    public void setBaby() {
        if (this.isAdult()) {
            this.setAge(-24000);
        }
    }
    
    @Override
    public void setAdult() {
        if (!this.isAdult()) {
            this.setAge(0);
        }
    }
    
    @Override
    public boolean isAdult() {
        return this.getAge() >= 0;
    }
    
    @Override
    public boolean canBreed() {
        return this.getAge() == 0;
    }
    
    @Override
    public void setBreed(final boolean breed) {
        if (breed) {
            this.setAge(0);
        }
        else if (this.isAdult()) {
            this.setAge(6000);
        }
    }
    
    @Override
    public EntityAgeable getHandle() {
        return (EntityAgeable)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftAgeable";
    }
}
