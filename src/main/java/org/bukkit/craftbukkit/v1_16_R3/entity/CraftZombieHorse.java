package org.bukkit.craftbukkit.v1_16_R3.entity;

import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.ZombieHorse;

public class CraftZombieHorse extends CraftAbstractHorse implements ZombieHorse {

    public CraftZombieHorse(CraftServer server, ZombieHorseEntity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftZombieHorse";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE_HORSE;
    }

    @Override
    public Variant getVariant() {
        return Variant.UNDEAD_HORSE;
    }
}
