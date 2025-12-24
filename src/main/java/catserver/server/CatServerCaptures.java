package catserver.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CatServerCaptures {
    private static final ThreadLocal<CatServerCaptures> catServerCaptures = new ThreadLocal<>();
    private Entity entity = null;
    private ItemEntity itemEntity = null;
    private CreatureSpawnEvent.SpawnReason spawnReason = null;
    private CreatureSpawnEvent.SpawnReason NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason = CreatureSpawnEvent.SpawnReason.DEFAULT;
    private boolean doPlace = true;
    private int createPortalRadius = 16;
    private BlockPos blockPos = null;
    private ItemStack itemstack = null;
    private Direction direction = null;
    private PlayerTeleportEvent.TeleportCause changeDimCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
    private PlayerTeleportEvent.TeleportCause teleportCause = PlayerTeleportEvent.TeleportCause.UNKNOWN;
    private org.bukkit.block.Block ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock = null;
    private CraftItemStack ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack = CraftItemStack.asCraftMirror(null);
    private EntityTransformEvent.TransformReason Mob$convertTo$transformReason = EntityTransformEvent.TransformReason.UNKNOWN;
    private CreatureSpawnEvent.SpawnReason Mob$convertTo$spawnReason = CreatureSpawnEvent.SpawnReason.DEFAULT;
    private EntityExhaustionEvent.ExhaustionReason Player$causeFoodExhaustion$reason = EntityExhaustionEvent.ExhaustionReason.UNKNOWN;
    private BrewingStandBlockEntity BrewingStandBlockEntity$doBrew$brewingStandBlockEntity = null;

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

    public void captureShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock(org.bukkit.block.Block ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock) {
        this.ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock = ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock;
    }

    public org.bukkit.block.Block getCaptureShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlockWithoutReset() {
        return this.ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock;
    }

    public void resetCaptureShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock() {
        this.ShearsDispenseItemBehavior$tryShearLivingEntity$bukkitBlock = null;
    }

    public void captureShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack(CraftItemStack ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack) {
        this.ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack = ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack;
    }

    public CraftItemStack getCaptureShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStackWithoutReset() {
        return this.ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack;
    }

    public void resetCaptureShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack() {
        this.ShearsDispenseItemBehavior$tryShearLivingEntity$craftItemStack = CraftItemStack.asCraftMirror(null);
    }

    public void captureMob$convertTo$transformReason(EntityTransformEvent.TransformReason Mob$convertTo$transformReason) {
        this.Mob$convertTo$transformReason = Mob$convertTo$transformReason;
    }

    public EntityTransformEvent.TransformReason getCaptureMob$convertTo$transformReason() {
        var result = this.Mob$convertTo$transformReason;
        this.Mob$convertTo$transformReason = EntityTransformEvent.TransformReason.UNKNOWN;
        return result;
    }

    public void captureMob$convertTo$spawnReason(CreatureSpawnEvent.SpawnReason Mob$convertTo$spawnReason) {
        this.Mob$convertTo$spawnReason = Mob$convertTo$spawnReason;
    }

    public CreatureSpawnEvent.SpawnReason getCaptureMob$convertTo$spawnReason() {
        var result = this.Mob$convertTo$spawnReason;
        this.Mob$convertTo$spawnReason = CreatureSpawnEvent.SpawnReason.DEFAULT;
        return result;
    }

    public void capturePlayer$causeFoodExhaustion$reason(EntityExhaustionEvent.ExhaustionReason Player$causeFoodExhaustion$reason) {
        this.Player$causeFoodExhaustion$reason = Player$causeFoodExhaustion$reason;
    }

    public EntityExhaustionEvent.ExhaustionReason getCapturePlayer$causeFoodExhaustion$reason() {
        var result = this.Player$causeFoodExhaustion$reason;
        this.Player$causeFoodExhaustion$reason = EntityExhaustionEvent.ExhaustionReason.UNKNOWN;
        return result;
    }

    public void captureBrewingStandBlockEntity$doBrew$brewingStandBlockEntity(BrewingStandBlockEntity BrewingStandBlockEntity$doBrew$brewingStandBlockEntity) {
        this.BrewingStandBlockEntity$doBrew$brewingStandBlockEntity = BrewingStandBlockEntity$doBrew$brewingStandBlockEntity;
    }

    public BrewingStandBlockEntity getCaptureBrewingStandBlockEntity$doBrew$brewingStandBlockEntity() {
        var result = this.BrewingStandBlockEntity$doBrew$brewingStandBlockEntity;
        this.BrewingStandBlockEntity$doBrew$brewingStandBlockEntity = null;
        return result;
    }

    public void captureNaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason(CreatureSpawnEvent.SpawnReason NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason) {
        this.NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason = NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason;
    }

    public CreatureSpawnEvent.SpawnReason getCaptureNaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason() {
        var result = this.NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason;
        this.NaturalSpawner$spawnCategoryForPosition$addFreshEntityWithPassengers$reason = CreatureSpawnEvent.SpawnReason.DEFAULT;
        return result;
    }
}
