package org.bukkit.craftbukkit.v1_20_R1.entity;

import net.minecraft.world.entity.animal.Pufferfish;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.entity.PufferFish;

public class CraftPufferFish extends CraftFish implements PufferFish {

    public CraftPufferFish(CraftServer server, Pufferfish entity) {
        super(server, entity);
    }

    @Override
    public Pufferfish getHandle() {
        return (Pufferfish) super.getHandle();
    }

    @Override
    public int getPuffState() {
        return getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        getHandle().setPuffState(state);
    }

    @Override
    public String toString() {
        return "CraftPufferFish";
    }
}
