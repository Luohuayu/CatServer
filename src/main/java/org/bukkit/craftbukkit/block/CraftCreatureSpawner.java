// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.block.Block;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.bukkit.block.CreatureSpawner;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner
{
    private final TileEntityMobSpawner spawner;
    
    public CraftCreatureSpawner(final Block block) {
        super(block);
        this.spawner = (TileEntityMobSpawner)((CraftWorld)block.getWorld()).getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }
    
    public CraftCreatureSpawner(final Material material, final TileEntityMobSpawner te) {
        super(material);
        this.spawner = te;
    }
    
    @Override
    public EntityType getSpawnedType() {
        return EntityType.fromName(this.spawner.getSpawnerBaseLogic().getEntityNameToSpawn());
    }
    
    @Override
    public void setSpawnedType(final EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }
        this.spawner.getSpawnerBaseLogic().setEntityName(entityType.getName());
    }
    
    @Deprecated
    public String getCreatureTypeId() {
        return this.spawner.getSpawnerBaseLogic().getEntityNameToSpawn();
    }
    
    @Deprecated
    public void setCreatureTypeId(final String creatureName) {
        this.setCreatureTypeByName(creatureName);
    }
    
    @Override
    public String getCreatureTypeName() {
        return this.spawner.getSpawnerBaseLogic().getEntityNameToSpawn();
    }
    
    @Override
    public void setCreatureTypeByName(final String creatureType) {
        final EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        this.setSpawnedType(type);
    }
    
    @Override
    public int getDelay() {
        return this.spawner.getSpawnerBaseLogic().spawnDelay;
    }
    
    @Override
    public void setDelay(final int delay) {
        this.spawner.getSpawnerBaseLogic().spawnDelay = delay;
    }
    
    @Override
    public TileEntityMobSpawner getTileEntity() {
        return this.spawner;
    }
}
