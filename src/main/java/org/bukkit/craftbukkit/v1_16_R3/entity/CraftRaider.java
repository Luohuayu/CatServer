package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.entity.Raider;

public abstract class CraftRaider extends CraftMonster implements Raider {

    public CraftRaider(CraftServer server, AbstractRaiderEntity entity) {
        super(server, entity);
    }

    @Override
    public AbstractRaiderEntity getHandle() {
        return (AbstractRaiderEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftRaider";
    }

    @Override
    public Block getPatrolTarget() {
        return getHandle().getPatrolTarget() == null ? null : CraftBlock.at(getHandle().level, getHandle().getPatrolTarget());
    }

    @Override
    public void setPatrolTarget(Block block) {
        if (block == null) {
            getHandle().setPatrolTarget((BlockPos) null);
        } else {
            Preconditions.checkArgument(block.getWorld().equals(this.getWorld()), "Block must be in same world");

            getHandle().setPatrolTarget(new BlockPos(block.getX(), block.getY(), block.getZ()));
        }
    }

    @Override
    public boolean isPatrolLeader() {
        return getHandle().isPatrolLeader();
    }

    @Override
    public void setPatrolLeader(boolean leader) {
        getHandle().setPatrolLeader(leader);
    }

    @Override
    public boolean isCanJoinRaid() {
        return getHandle().canJoinRaid();
    }

    @Override
    public void setCanJoinRaid(boolean join) {
        getHandle().setCanJoinRaid(join);
    }
}
