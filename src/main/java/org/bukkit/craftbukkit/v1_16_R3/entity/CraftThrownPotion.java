package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, PotionEntity entity) {
        super(server, entity);
    }

    @Override
    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (EffectInstance effect : PotionUtils.getMobEffects(getHandle().getItem())) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        // The ItemStack must not be null.
        Validate.notNull(item, "ItemStack cannot be null.");

        // The ItemStack must be a potion.
        Validate.isTrue(item.getType() == Material.LINGERING_POTION || item.getType() == Material.SPLASH_POTION, "ItemStack must be a lingering or splash potion. This item stack was " + item.getType() + ".");

        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public PotionEntity getHandle() {
        return (PotionEntity) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.SPLASH_POTION;
    }
}
