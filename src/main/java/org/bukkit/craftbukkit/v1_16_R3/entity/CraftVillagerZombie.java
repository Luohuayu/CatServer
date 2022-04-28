package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class CraftVillagerZombie extends CraftZombie implements ZombieVillager {

    public CraftVillagerZombie(CraftServer server, ZombieVillagerEntity entity) {
        super(server, entity);
    }

    @Override
    public ZombieVillagerEntity getHandle() {
        return (ZombieVillagerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftVillagerZombie";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return Villager.Profession.valueOf(Registry.VILLAGER_PROFESSION.getKey(getHandle().getVillagerData().getProfession()).getPath().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        Validate.notNull(profession);
        getHandle().setVillagerData(getHandle().getVillagerData().setProfession(Registry.VILLAGER_PROFESSION.get(new ResourceLocation(profession.name().toLowerCase(Locale.ROOT)))));
    }

    @Override
    public Villager.Type getVillagerType() {
        return Villager.Type.valueOf(Registry.VILLAGER_TYPE.getKey(getHandle().getVillagerData().getType()).getPath().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerType(Villager.Type type) {
        Validate.notNull(type);
        getHandle().setVillagerData(getHandle().getVillagerData().setType(Registry.VILLAGER_TYPE.get(CraftNamespacedKey.toMinecraft(type.getKey()))));
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");

        return getHandle().conversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().conversionTime = -1;
            getHandle().getEntityData().set(ZombieVillagerEntity.DATA_CONVERTING_ID, false);
            getHandle().persistenceRequired = false; // CraftBukkit - SPIGOT-4684 update persistence
            getHandle().conversionStarter = null;
            getHandle().removeEffect(Effects.DAMAGE_BOOST, EntityPotionEffectEvent.Cause.CONVERSION);
        } else {
            getHandle().startConverting((UUID) null, time);
        }
    }

    @Override
    public OfflinePlayer getConversionPlayer() {
        return (getHandle().conversionStarter == null) ? null : Bukkit.getOfflinePlayer(getHandle().conversionStarter);
    }

    @Override
    public void setConversionPlayer(OfflinePlayer conversionPlayer) {
        if (!this.isConverting()) return;
        getHandle().conversionStarter = (conversionPlayer == null) ? null : conversionPlayer.getUniqueId();
    }
}
