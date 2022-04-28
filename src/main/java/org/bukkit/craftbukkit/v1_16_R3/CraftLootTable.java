package org.bukkit.craftbukkit.v1_16_R3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
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
        net.minecraft.loot.LootContext nmsContext = convertContext(context);
        List<net.minecraft.item.ItemStack> nmsItems = handle.getRandomItems(nmsContext);
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.item.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        net.minecraft.loot.LootContext nmsContext = convertContext(context);
        CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fill(handle, nmsContext);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private net.minecraft.loot.LootContext convertContext(LootContext context) {
        Location loc = context.getLocation();
        ServerWorld handle = ((CraftWorld) loc.getWorld()).getHandle();

        net.minecraft.loot.LootContext.Builder builder = new net.minecraft.loot.LootContext.Builder(handle);
        setMaybe(builder, LootParameters.ORIGIN, new Vector3d(loc.getX(), loc.getY(), loc.getZ()));
        if (getHandle() != LootTable.EMPTY) {
            // builder.luck(context.getLuck());

            if (context.getLootedEntity() != null) {
                Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
                setMaybe(builder, LootParameters.THIS_ENTITY, nmsLootedEntity);
                setMaybe(builder, LootParameters.DAMAGE_SOURCE, DamageSource.GENERIC);
                setMaybe(builder, LootParameters.POSITION, new BlockPos(nmsLootedEntity));
            }

            if (context.getKiller() != null) {
                PlayerEntity nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
                setMaybe(builder, LootParameters.KILLER_ENTITY, nmsKiller);
                // If there is a player killer, damage source should reflect that in case loot tables use that information
                setMaybe(builder, LootParameters.DAMAGE_SOURCE, DamageSource.playerAttack(nmsKiller));
                setMaybe(builder, LootParameters.LAST_DAMAGE_PLAYER, nmsKiller); // SPIGOT-5603 - Set minecraft:killed_by_player
            }

            // SPIGOT-5603 - Use LootContext#lootingModifier
            if (context.getLootingModifier() != LootContext.DEFAULT_LOOT_MODIFIER) {
                setMaybe(builder, LootParameters.LOOTING_MOD, context.getLootingModifier());
            }
        }

        // SPIGOT-5603 - Avoid IllegalArgumentException in LootTableInfo#build()
        LootParameterSet.Builder nmsBuilder = new LootParameterSet.Builder(); // PAIL rename Builder
        for (LootParameter<?> param : getHandle().getParamSet().getAllowed()) { // PAIL rename required
            nmsBuilder.required(param); // PAIL rename addRequired
        }
        for (LootParameter<?> param : getHandle().getParamSet().getAllowed()) { // PAIL rename optional
            if (!getHandle().getParamSet().getAllowed().contains(param)) { // PAIL rename required
                nmsBuilder.optional(param); // PAIL rename addOptional
            }
        }
        nmsBuilder.optional(LootParameters.LOOTING_MOD);

        return builder.create(nmsBuilder.build());
    }

    private <T> void setMaybe(net.minecraft.loot.LootContext.Builder builder, LootParameter<T> param, T value) {
        if (getHandle().getParamSet().getRequired().contains(param) || getHandle().getParamSet().getAllowed().contains(param)) {
            builder.withParameter(param, value);
        }
    }

    public static LootContext convertContext(net.minecraft.loot.LootContext info) {
        Vector3d position = info.getParamOrNull(LootParameters.ORIGIN);
        if (position == null) {
            position = info.getParamOrNull(LootParameters.THIS_ENTITY).position(); // Every vanilla context has origin or this_entity, see LootParameters
        }
        Location location = new Location(info.getLevel().getWorld(), position.x(), position.y(), position.z());
        LootContext.Builder contextBuilder = new LootContext.Builder(location);

        if (info.hasParam(LootParameters.KILLER_ENTITY)) {
            CraftEntity killer = info.getParamOrNull(LootParameters.KILLER_ENTITY).getBukkitEntity();
            if (killer instanceof CraftHumanEntity) {
                contextBuilder.killer((CraftHumanEntity) killer);
            }
        }

        if (info.hasParam(LootParameters.THIS_ENTITY)) {
            contextBuilder.lootedEntity(info.getParamOrNull(LootParameters.THIS_ENTITY).getBukkitEntity());
        }

        if (info.hasParam(LootParameters.LOOTING_MOD)) {
            contextBuilder.lootingModifier(info.getParamOrNull(LootParameters.LOOTING_MOD));
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
