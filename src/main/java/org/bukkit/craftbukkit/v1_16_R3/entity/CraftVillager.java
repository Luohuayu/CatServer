package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import moe.loliserver.BukkitInjector;
import java.util.Locale;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public class CraftVillager extends CraftAbstractVillager implements Villager {

    public CraftVillager(CraftServer server, VillagerEntity entity) {
        super(server, entity);
    }

    @Override
    public VillagerEntity getHandle() {
        return (VillagerEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    @Override
    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    @Override
    public Profession getProfession() {
        return CraftVillager.nmsToBukkitProfession(getHandle().getVillagerData().getProfession());
    }

    @Override
    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        getHandle().setVillagerData(getHandle().getVillagerData().setProfession(CraftVillager.bukkitToNmsProfession(profession)));
    }

    @Override
    public Type getVillagerType() {
        return Type.valueOf(Registry.VILLAGER_TYPE.getKey(getHandle().getVillagerData().getType()).getPath().toUpperCase(Locale.ROOT));
    }

    @Override
    public void setVillagerType(Type type) {
        Validate.notNull(type);
        getHandle().setVillagerData(getHandle().getVillagerData().setType(Registry.VILLAGER_TYPE.get(CraftNamespacedKey.toMinecraft(type.getKey()))));
    }

    @Override
    public int getVillagerLevel() {
        return getHandle().getVillagerData().getLevel();
    }

    @Override
    public void setVillagerLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 5, "level must be between [1, 5]");

        getHandle().setVillagerData(getHandle().getVillagerData().setLevel(level));
    }

    @Override
    public int getVillagerExperience() {
        return getHandle().getVillagerXp();
    }

    @Override
    public void setVillagerExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "Experience must be positive");

        getHandle().setVillagerXp(experience);
    }

    @Override
    public boolean sleep(Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "Cannot sleep across worlds");

        BlockPos position = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        BlockState iblockdata = getHandle().level.getBlockState(position);
        if (!(iblockdata.getBlock() instanceof BedBlock)) {
            return false;
        }

        getHandle().startSleeping(position);
        return true;
    }

    @Override
    public void wakeup() {
        Preconditions.checkState(isSleeping(), "Cannot wakeup if not sleeping");

        getHandle().stopSleeping();
    }

    public static Profession nmsToBukkitProfession(VillagerProfession nms) {
        return nms.getRegistryName().getNamespace().equals(NamespacedKey.MINECRAFT) ? Profession.valueOf(Registry.VILLAGER_PROFESSION.getKey(nms).getPath().toUpperCase(Locale.ROOT)) : Profession.valueOf(BukkitInjector.normalizeName(nms.getRegistryName().toString()));
    }

    public static VillagerProfession bukkitToNmsProfession(Profession bukkit) {
        return !BukkitInjector.profession.containsKey(bukkit) ? Registry.VILLAGER_PROFESSION.get(CraftNamespacedKey.toMinecraft(bukkit.getKey())) : ForgeRegistries.PROFESSIONS.getValue(BukkitInjector.profession.get(bukkit));
    }
}
