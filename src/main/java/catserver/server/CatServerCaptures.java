package catserver.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CatServerCaptures {
    private static final ThreadLocal<CatServerCaptures> catServerCaptures = new ThreadLocal<>();
    private Entity entity = null;
    private ItemEntity itemEntity = null;
    private CreatureSpawnEvent.SpawnReason spawnReason = null;
    private boolean doPlace = true;
    private int createPortalRadius = 16;
    private BlockPos blockPos = null;
    private ItemStack itemstack = null;
    private Direction direction = null;
    private PlayerTeleportEvent.TeleportCause changeDimCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
    private PlayerTeleportEvent.TeleportCause teleportCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;

    public static CatServerCaptures getCatServerCaptures() {
        CatServerCaptures currentThreadCaptures = catServerCaptures.get();
        if (currentThreadCaptures == null) {
            currentThreadCaptures = new CatServerCaptures();
            catServerCaptures.set(currentThreadCaptures);
        }
        return currentThreadCaptures;
    }

    public void captureEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getCaptureEntity() {
        var result = this.entity;
        this.entity = null;
        return result;
    }

    public void captureItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public ItemEntity getCaptureItemEntity() {
        var result = this.itemEntity;
        this.itemEntity = null;
        return result;
    }

    public void captureSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        this.spawnReason = spawnReason;
    }

    public CreatureSpawnEvent.SpawnReason getCaptureSpawnReason() {
        var result = this.spawnReason;
        this.spawnReason = null;
        return result;
    }

    public void captureDoPlace(boolean doPlace) {
        this.doPlace = doPlace;
    }

    public boolean getCaptureDoPlace() {
        var result = this.doPlace;
        this.doPlace = true;
        return result;
    }

    public void capturePortalRadius(int i) {
        this.createPortalRadius = i;
    }

    public int getCapturePortalRadius() {
        var result = this.createPortalRadius;
        this.createPortalRadius = 16;
        return result;
    }

    public void captureTeleportCause(PlayerTeleportEvent.TeleportCause cause) {
        this.teleportCause = cause;
    }

    public PlayerTeleportEvent.TeleportCause getCaptureTeleportCause() {
        var result = this.teleportCause;
        this.teleportCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
        return result;
    }

    public void captureBlockPos(BlockPos pos) {
        this.blockPos = pos;
    }

    public BlockPos getCaptureBlockPos() {
        var result = this.blockPos;
        this.blockPos = null;
        return result;
    }

    public void captureItemStack(ItemStack itemstack) {
        this.itemstack = itemstack;
    }

    public ItemStack getCaptureItemStack() {
        var result = this.itemstack;
        this.itemstack = null;
        return result;
    }

    public void captureDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getCaptureDirection() {
        var result = this.direction;
        this.direction = null;
        return result;
    }

    public void captureChangeDimCause(PlayerTeleportEvent.TeleportCause cause) {
        this.changeDimCause = cause;
    }

    public PlayerTeleportEvent.TeleportCause getCaptureChangeDimCause() {
        var result = this.changeDimCause;
        this.changeDimCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
        return result;
    }
}
