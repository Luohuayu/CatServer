// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder
{
    private final World world;
    private final net.minecraft.world.border.WorldBorder handle;
    
    public CraftWorldBorder(final CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().getWorldBorder();
    }
    
    @Override
    public void reset() {
        this.setSize(6.0E7);
        this.setDamageAmount(0.2);
        this.setDamageBuffer(5.0);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0.0, 0.0);
    }
    
    @Override
    public double getSize() {
        return this.handle.getDiameter();
    }
    
    @Override
    public void setSize(final double newSize) {
        this.setSize(newSize, 0L);
    }
    
    @Override
    public void setSize(double newSize, long time) {
        newSize = Math.min(6.0E7, Math.max(1.0, newSize));
        time = Math.min(9223372036854775L, Math.max(0L, time));
        if (time > 0L) {
            this.handle.setTransition(this.handle.getDiameter(), newSize, time * 1000L);
        }
        else {
            this.handle.setTransition(newSize);
        }
    }
    
    @Override
    public Location getCenter() {
        final double x = this.handle.getCenterX();
        final double z = this.handle.getCenterZ();
        return new Location(this.world, x, 0.0, z);
    }
    
    @Override
    public void setCenter(double x, double z) {
        x = Math.min(3.0E7, Math.max(-3.0E7, x));
        z = Math.min(3.0E7, Math.max(-3.0E7, z));
        this.handle.setCenter(x, z);
    }
    
    @Override
    public void setCenter(final Location location) {
        this.setCenter(location.getX(), location.getZ());
    }
    
    @Override
    public double getDamageBuffer() {
        return this.handle.getDamageBuffer();
    }
    
    @Override
    public void setDamageBuffer(final double blocks) {
        this.handle.setDamageBuffer(blocks);
    }
    
    @Override
    public double getDamageAmount() {
        return this.handle.getDamageAmount();
    }
    
    @Override
    public void setDamageAmount(final double damage) {
        this.handle.setDamageAmount(damage);
    }
    
    @Override
    public int getWarningTime() {
        return this.handle.getWarningTime();
    }
    
    @Override
    public void setWarningTime(final int time) {
        this.handle.setWarningTime(time);
    }
    
    @Override
    public int getWarningDistance() {
        return this.handle.getWarningDistance();
    }
    
    @Override
    public void setWarningDistance(final int distance) {
        this.handle.setWarningDistance(distance);
    }
}
