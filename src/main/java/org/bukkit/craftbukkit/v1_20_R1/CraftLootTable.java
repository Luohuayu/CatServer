package org.bukkit.craftbukkit.v1_20_R1;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftLocation;
import org.bukkit.craftbukkit.v1_20_R1.util.RandomSourceWrapper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

public class CraftLootTable implements org.bukkit.loot.LootTable {

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(NamespacedKey key, LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        LootParams nmsContext = convertContext(context, random);
        List<net.minecraft.world.item.ItemStack> nmsItems = handle.getRandomItems(nmsContext);
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.world.item.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        Preconditions.checkArgument(inventory != null, "Inventory cannot be null");
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        LootParams nmsContext = convertContext(context, random);
        CraftInventory craftInventory = (CraftInventory) inventory;
        Container handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fillInventory(handle, nmsContext, random.nextLong(), true);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private LootParams convertContext(LootContext context, Random random) {
        Preconditions.checkArgument(context != null, "LootContext cannot be null");
        Location loc = context.getLocation();
        Preconditions.checkArgument(loc.getWorld() != null, "LootContext.getLocation#getWorld cannot be null");
        ServerLevel handle = ((CraftWorld) loc.getWorld()).getHandle();

        LootParams.Builder builder = new LootParams.Builder(handle);
        if (random != null) {
            // builder = builder.withRandom(new RandomSourceWrapper(random));
        }
        setMaybe(builder, LootContextParams.ORIGIN, CraftLocation.toVec3D(loc));
        if (getHandle() != LootTable.EMPTY) {
            // builder.luck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                setMaybe(builder, LootContextParams.THIS_ENTITY, nmsLootedEntity);
                setMaybe(builder, LootContextParams.DAMAGE_SOURCE, handle.damageSources().generic());
                setMaybe(builder, LootContextParams.ORIGIN, nmsLootedEntity.position());
            }

            if (context.getKiller() != null) {
                Player nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                setMaybe(builder, LootContextParams.KILLER_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                setMaybe(builder, LootContextParams.DAMAGE_SOURCE, handle.damageSources().playerAttack(nmsKiller));
                setMaybe(builder, LootContextParams.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
                setMaybe(builder, LootContextParams.TOOL, nmsKiller.getUseItem()); // SPIGOT-6925 - Set minecraft:match_tool
            }

            // SPIGOT-5603 - Use LootContext#lootingModifier
            if (context.getLootingModifier() != LootContext.DEFAULT_LOOT_MODIFIER) {
                setMaybe(builder, LootContextParams.LOOTING_MOD, context.getLootingModifier());
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in LootTableInfo#build()
        LootContextParamSet.Builder nmsBuilder = new LootContextParamSet.Builder();
        for (LootContextParam<?> param : getHandle().getParamSet().getRequired()) {
            nmsBuilder.required(param);
        }
        for (LootContextParam<?> param : getHandle().getParamSet().getAllowed()) {
            if (!getHandle().getParamSet().getRequired().contains(param)) {
                nmsBuilder.optional(param);
            }
        }
        nmsBuilder.optional(LootContextParams.LOOTING_MOD);

        return builder.create(getHandle().getParamSet());
    }

    private <T> void setMaybe(LootParams.Builder builder, LootContextParam<T> param, T value) {
        if (getHandle().getParamSet().getRequired().contains(param) || getHandle().getParamSet().getAllowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }

    public static LootContext convertContext(net.minecraft.world.level.storage.loot.LootContext info) {
        Vec3 position = info.getParamOrNull(LootContextParams.ORIGIN);
        if (position == null) {
            if (!info.hasParam(LootContextParams.THIS_ENTITY)) position = new Vec3(0, 0, 0); else // CatServer - prevent npe
                position = info.getParamOrNull(LootContextParams.THIS_ENTITY).position(); // Every vanilla context has origin or this_entity, see LootContextParameterSets
        }
        Location location = CraftLocation.toBukkit(position, info.getLevel().getWorld());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.hasParam(LootContextParams.KILLER_ENTITY)) {
            CraftEntity killer = info.getParamOrNull(LootContextParams.KILLER_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.hasParam(LootContextParams.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.getParamOrNull(LootContextParams.THIS_ENTITY).getBukkitEntity());
        }

        if (info.hasParam(LootContextParams.LOOTING_MOD)) {
            contextBuilder.lootingModifier(info.getParamOrNull(LootContextParams.LOOTING_MOD));
        }

        contextBuilder.luck(info.getLuck());
        return contextBuilder.build();
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof org.bukkit.loot.LootTable)) {
            return false;
        }

        org.bukkit.loot.LootTable table = (org.bukkit.loot.LootTable) obj;
        return table.getKey().equals(this.getKey());
    }
}
