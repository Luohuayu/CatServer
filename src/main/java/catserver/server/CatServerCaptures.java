package catserver.server;

import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CatServerCaptures {
    private static final CatServerCaptures catServerCaptures = new CatServerCaptures();
    private AtomicReference<Entity> entity = new AtomicReference<>();
    private AtomicReference<ItemEntity> itemEntity = new AtomicReference<>();
    private AtomicReference<CreatureSpawnEvent.SpawnReason> spawnReason = new AtomicReference<>();
    private AtomicBoolean doPlace = new AtomicBoolean(true);
    private AtomicInteger spawnerLimit = new AtomicInteger();
    private AtomicInteger createPortalRadius = new AtomicInteger();
    private AtomicReference<BlockPos> blockPos = new AtomicReference<>();
    private AtomicReference<ItemStack> itemstack = new AtomicReference<>();
    private AtomicReference<PlayerTeleportEvent.TeleportCause> changeDimCause = new AtomicReference<>();
    private AtomicInteger searchPortalRadius = new AtomicInteger();
    private AtomicBoolean canCreatePortal = new AtomicBoolean();
    private AtomicBoolean isForceSleep = new AtomicBoolean(false);
    private AtomicBoolean isCallEvent = new AtomicBoolean(true);
    private AtomicBoolean isSilent = new AtomicBoolean(false);
    private AtomicReference<Direction> direction = new AtomicReference<>();
    private AtomicReference<BlockState> blockState = new AtomicReference<>();
    private AtomicReference<ServerPlayer> serverPlayer = new AtomicReference<>();
    private AtomicReference<Level> level =  new AtomicReference<>();
    private AtomicDouble blockRange = new AtomicDouble();
    private AtomicBoolean dropper = new AtomicBoolean(false);
    private AtomicReference<EntityRegainHealthEvent.RegainReason> regainReason = new AtomicReference<>(EntityRegainHealthEvent.RegainReason.CUSTOM);
    private AtomicReference<EntityPotionEffectEvent.Cause> potionEffectCause = new AtomicReference<>(EntityPotionEffectEvent.Cause.UNKNOWN);
    private AtomicReference<PlayerTeleportEvent.TeleportCause> teleportCause = new AtomicReference<>(PlayerTeleportEvent.TeleportCause.UNKNOWN);
    private AtomicBoolean arrowFlag = new AtomicBoolean(false);

    public void captureTeleportCause(PlayerTeleportEvent.TeleportCause cause) {
        this.teleportCause.set(cause);
    }

    public void captureArrowFlag(boolean flag) {
        this.arrowFlag.set(flag);
    }

    public void capturePotionEffectCause(EntityPotionEffectEvent.Cause cause) {
        this.potionEffectCause.set(cause);
    }

    public void captureRegainReason(EntityRegainHealthEvent.RegainReason regainReason) {
        this.regainReason.set(regainReason);
    }

    public void captureDropper(boolean dropper) {
        this.dropper.set(dropper);
    }

    public void captureBlockRange(double d) {
        this.blockRange.set(d);
    }

    public void captureEntity(Entity entity) {
        this.entity.set(entity);
    }

    public void captureServerPlayer(ServerPlayer player) {
        this.serverPlayer.set(player);
    }

    public Entity getCaptureEntity() {
        return this.entity.getAndSet(null);
    }

    public void captureSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        this.spawnReason.set(spawnReason);
    }

    public void captureSpawnerLimit(int i) {
        this.spawnerLimit.set(i);
    }

    public void capturePortalRadius(int i) {
        this.createPortalRadius.set(i);
    }

    public void captureBlockPos(BlockPos pos) {
        this.blockPos.set(pos);
    }
    public void captureItemStack(ItemStack itemstack) {
        this.itemstack.set(itemstack);
    }
    public void captureDirection(Direction direction) {
        this.direction.set(direction);
    }

    public void captureChangeDimCause(PlayerTeleportEvent.TeleportCause cause) {
        this.changeDimCause.set(cause);
    }

    public void capturePortalSearchRadius(int i) {
        this.searchPortalRadius.set(i);
    }

    public void captureCanCreatePortal(boolean value) {
        this.canCreatePortal.set(value);
    }
    public void captureIsForceSleep(boolean value) {
        this.isForceSleep.set(value);
    }
    public void captureIsCallEvent(boolean value) {
        this.isCallEvent.set(value);
    }
    public void captureIsSlient(boolean isSlient) {
        this.isSilent.set(isSlient);
    }
    public void captureBlockState(BlockState blockState) {
        this.blockState.set(blockState);
    }
    public void captureItemEntity(ItemEntity itemEntity) {
        this.itemEntity.set(itemEntity);
    }
    public void captureDoPlace(boolean doPlace) {
        this.doPlace.set(doPlace);
    }
    public void captureLevel(Level level) {
        this.level.set(level);
    }

    public EntityRegainHealthEvent.RegainReason getCaptureRegainReason() {
        return this.regainReason.getAndSet(EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public Level getCaptureLevel() {
        return this.level.getAndSet(null);
    }

    public CreatureSpawnEvent.SpawnReason getCaptureSpawnReason() {
        return this.spawnReason.getAndSet(null);
    }

    public boolean getCaptureDoPlace() {
        return this.doPlace.getAndSet(true);
    }

    public int getCaptureLimit() {
        return this.spawnerLimit.getAndSet(0);
    }

    public BlockState getCaptureBlockState() {
        return this.blockState.getAndSet(null);
    }

    public int getCapturePortalRadius() {
        return this.createPortalRadius.getAndSet(0);
    }

    public BlockPos getCaptureBlockPos() {
        return this.blockPos.getAndSet(null);
    }

    public ItemStack getCaptureItemStack() {
        return this.itemstack.getAndSet(null);
    }

    public Direction getCaptureDirection() {
        return this.direction.getAndSet(null);
    }

    public PlayerTeleportEvent.TeleportCause getCaptureChangeDimCause() {
        return this.changeDimCause.getAndSet(null);
    }

    public int getCapturePortalSearchRadius() {
        return this.searchPortalRadius.getAndSet(0);
    }

    public boolean getCaptureCanCreatePortal() {
        return this.canCreatePortal.getAndSet(false);
    }

    public boolean getCaptureIsForceSleep() {
        return this.isForceSleep.getAndSet(false);
    }

    public boolean getCaptureIsCallEvent() {
        return this.isCallEvent.getAndSet(true);
    }
    public boolean getCaptureIsSlient() {
        return this.isSilent.getAndSet(false);
    }
    public ItemEntity getCaptureItemEntity() {
        return this.itemEntity.getAndSet(null);
    }
    public ServerPlayer getCaptureServerPlayer() {
        return this.serverPlayer.getAndSet(null);
    }
    public double getCaptureBlockRange() {
        return this.blockRange.getAndSet(0.0D);
    }

    public boolean getCaptureDropper() {
        return this.dropper.getAndSet(false);
    }

    public EntityPotionEffectEvent.Cause getCapturePotionEffectCause() {
        return this.potionEffectCause.getAndSet(EntityPotionEffectEvent.Cause.UNKNOWN);
    }

    public boolean getCaptureArrowFlag() {
        return this.arrowFlag.getAndSet(false);
    }

    public PlayerTeleportEvent.TeleportCause getCaptureTeleportCause() {
        return this.teleportCause.getAndSet(PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public static CatServerCaptures getCatServerCaptures() {
        return catServerCaptures;
    }
}
