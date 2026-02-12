package catserver.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;
import net.minecraft.world.level.portal.PortalForcer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnReason;
import org.bukkit.event.player.PlayerSpawnChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class CatServerCaptures {
    private static final ThreadLocal<CatServerCaptures> catServerCaptures = new ThreadLocal<>();

    public final CatCaptureValueStack<PortalForcer, Entity> PortalForcer$createPortal$entity = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<PortalForcer, Integer> PortalForcer$createPortal$createRadius = withDefaultValue(16);
    public final CatCaptureValueStack<Mob, ItemEntity> Mob$equipItemIfPossible$itemEntity = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<ServerLevelAccessor, CreatureSpawnEvent.SpawnReason> ServerLevelAccessor$addFreshEntityWithPassengers$reason = withDefaultValue(CreatureSpawnEvent.SpawnReason.DEFAULT);
    public final CatCaptureValueStack<ServerLevel, CreatureSpawnEvent.SpawnReason> ServerLevel$addEntity$spawnReason = withDefaultValue(CreatureSpawnEvent.SpawnReason.DEFAULT);
    public final CatCaptureValueStack<Mob, EntityTransformEvent.TransformReason> Mob$convertTo$transformReason = withDefaultValue(EntityTransformEvent.TransformReason.UNKNOWN);
    public final CatCaptureValueStack<Mob, CreatureSpawnEvent.SpawnReason> Mob$convertTo$spawnReason = withDefaultValue(CreatureSpawnEvent.SpawnReason.DEFAULT);
    public final CatCaptureValueStack<Level, CreatureSpawnEvent.SpawnReason> ServerLevel$addFreshEntity$reason = withDefaultValue(CreatureSpawnEvent.SpawnReason.DEFAULT);
    public final CatCaptureValueStack<LevelChunk, Boolean> LevelChunk$setBlockState$doPlace = withDefaultValue(true);
    public final CatCaptureValueStack<FireBlock, BlockPos> FireBlock$tryCatchFire$bukkitPos = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<ServerPlayer, PlayerTeleportEvent.TeleportCause> ServerPlayer$changeDimension$cause = withDefaultValue(PlayerTeleportEvent.TeleportCause.UNKNOWN);
    public final CatCaptureValueStack<LivingEntity, PlayerTeleportEvent.TeleportCause> LivingEntity$randomTeleport$cause = withDefaultValue(PlayerTeleportEvent.TeleportCause.UNKNOWN);
    public final CatCaptureValueStack<Void, org.bukkit.block.Block> ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock = withDefaultValue(null); // NotNull but without NPE
    public final CatCaptureValueStack<Void, CraftItemStack> ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack = withDefaultValue(() -> CraftItemStack.asCraftMirror(/*Nullable*/null));
    public final CatCaptureValueStack<Player, EntityExhaustionEvent.ExhaustionReason> Player$causeFoodExhaustion$reason = withDefaultValue(EntityExhaustionEvent.ExhaustionReason.UNKNOWN);
    public final CatCaptureValueStack<Void, BrewingStandBlockEntity> BrewingStandBlockEntity$doBrew$tileentitybrewingstand = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<Void, Entity> RedStoneOreBlock$interact$entity = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<ServerPlayer, PlayerSpawnChangeEvent.Cause> ServerPlayer$startSleepInBed$setRespawnPosition$cause = withDefaultValue(PlayerSpawnChangeEvent.Cause.UNKNOWN); // Nullable
    public final CatCaptureValueStack<LivingEntity, Boolean> LivingEntity$actuallyHurt$return = withDefaultValue(false);
    public final CatCaptureValueStack<LivingEntity, Boolean> LivingEntity$getDamageAfterArmorAbsorb$flag_bypassHurtArmor = withDefaultValue(false);
    public final CatCaptureValueStack<LivingEntity, Boolean> LivingEntity$getDamageAfterArmorAbsorb$flag_onlyHurtArmor = withDefaultValue(false);
    public final CatCaptureValueStack<PlayerList, RespawnReason> PlayerList$respawn$reason = withDefaultValue(RespawnReason.DEATH);
    public final CatCaptureValueStack<PlayerList, ServerLevel> PlayerList$respawn$worldServer = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<PlayerList, Location> PlayerList$respawn$location = withDefaultValue(null); // Nullable
    public final CatCaptureValueStack<PlayerList, Boolean> PlayerList$respawn$avoidSuffocation = withDefaultValue(true);
    public final CatCaptureValueStack<RegionFileStorage, Boolean> RegionFileStorage$getRegionFile$existingOnly = withDefaultValue(false);
    public final CatCaptureValueStack<Level, Boolean> Level$explode_EntityDDDFExplosionInteraction$p_256634_ = withDefaultValue(false);
    public final CatCaptureValueStack<ServerGamePacketListenerImpl, Integer> ServerGamePacketListenerImpl$updateBookPages$slot = withDefaultValue(-1);
    public final CatCaptureValueStack<ServerGamePacketListenerImpl, ItemStack> ServerGamePacketListenerImpl$updateBookPages$handItem = withDefaultValue(ItemStack.EMPTY);

    public static CatServerCaptures getCatServerCaptures() {
        CatServerCaptures currentThreadCaptures = catServerCaptures.get();
        if (currentThreadCaptures == null) {
            currentThreadCaptures = new CatServerCaptures();
            catServerCaptures.set(currentThreadCaptures);
        }
        return currentThreadCaptures;
    }

    public static <H, V> CatCaptureValueStack<H, V> withDefaultValue(V defaultValue) {
        return new CatCaptureValueStack<>(defaultValue);
    }

    public static <H, V> CatCaptureValueStack<H, V> withDefaultValue(Supplier<V> defaultValueSupplier) {
        return new CatCaptureValueStack<>(defaultValueSupplier);
    }

    /**
     * To ensure one value is only used at one correct place, for each instance, #push() can only be called at one place and #pop() can only be called at one place, both in the craftbukkit method:
     * <p>craftbukkitMethod(value) {
     * <p>    try {
     * <p>        push(value);
     * <p>        vanillaMethod();
     * <p>    } finally {
     * <p>        pop();
     * <p>    }
     * <p>}
     * <p>vanillaMethod() {
     * <p>    value = get(); // Don't call #get() too early and compat with mixin local variable
     * <p>}
     * <p>
     * Consider unusual cases:
     * The vanilla method throws an exception before we get the craftbukkit value.
     * A mod mixin-injects at the HEAD of a method, before the craftbukkit value be got, and the mixin code throws an exception.
     * <p>
     * If the bukkit method is called inside itself, the stack can ensure the independence of the values of each call.
     * However, if a vanilla method without the additional argument is called inside it's craftbukkit method and the context holder objects are same,
     * the vanilla method will get the capture value which was set by the outer craftbukkit method, instead of the default value.
     * <p>craftbukkitMethod(value) {
     * <p>    try {
     * <p>        push(obj0, value);
     * <p>        obj0.vanillaMethod();
     * <p>    } finally {
     * <p>        pop(obj0);
     * <p>    }
     * <p>}
     * <p>vanillaMethod() {
     * <p>    value = get(obj0); // At the second time this method called, here we will get the value of the first call but not the default value.
     * <p>    if (...) {
     * <p>        obj0.vanillaMethod();
     * <p>    }
     * <p>}
     * <p>Seems this is an unsolvable problem(?) QwQ
     * <p>
     * <p>the holder can be null only if this is used at a static call context
     */
    public static class CatCaptureValueStack<H, V> {
        private final LinkedList<SimpleEntry<H, V>> stack = new LinkedList<>();
        private final Supplier<V> defaultValueSupplier;

        private CatCaptureValueStack(V defaultValue) {
            this.defaultValueSupplier = () -> defaultValue;
        }

        private CatCaptureValueStack(Supplier<V> defaultValueSupplier) {
            this.defaultValueSupplier = defaultValueSupplier;
        }

        public void push(H holder, V value) {
            this.stack.push(new SimpleEntry<>(holder, value));
        }

        public V get(H holder) {
            if (!this.stack.isEmpty()) {
                SimpleEntry<H, V> entry = this.stack.peek();
                if (entry.getKey() == holder) {
                    return entry.getValue();
                }
            }
            return this.defaultValueSupplier.get();
        }

        /** This is only used in try-finally to ensure reset and returns nothing. */
        public void pop(H holder) {
            try {
                SimpleEntry<H, V> entry = this.stack.peek();
                if (entry.getKey() != holder) {
                    CatServer.LOGGER.error("[CatServer Captures] [" + holder + "] is removing its capture value [" + entry.getValue() + "] but the value does not belong to it.", new IllegalArgumentException());
                } else {
                    this.stack.pop();
                }
            } catch (NoSuchElementException e) {
                CatServer.LOGGER.error("[CatServer Captures] Removing a missing capture value of [" + holder + "].", e);
            }
        }

        public void replaceOrDoNothing(H holder, V value) {
            if (!this.stack.isEmpty()) {
                SimpleEntry<H, V> entry = this.stack.peek();
                if (entry.getKey() == holder) {
                    entry.setValue(value);
                }
            }
        }
    }
}
