package catserver.server;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;
import java.util.List;

public class CatServerEventHandler {
    public static final BukkitEventCapture<BlockBreakEvent> bukkitBlockBreakEventCapture = new BukkitEventCapture<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockBreakEvent bukkitEvent = CraftEventFactory.callBlockBreakEvent(event.getWorld(), event.getPos(), event.getState(), (EntityPlayerMP) event.getPlayer());

        if (bukkitEvent.isCancelled()) {
            event.setCanceled(true);
        } else {
            event.setExpToDrop(bukkitEvent.getExpToDrop());
        }

        bukkitBlockBreakEventCapture.put(bukkitEvent);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        BlockPos clickPos = event.direction != null ? event.getBlockSnapshot().getPos().offset(event.direction.getOpposite()) : event.getPos();

        BlockPlaceEvent bukkitEvent;
        if (event instanceof BlockEvent.MultiPlaceEvent) {
            BlockEvent.MultiPlaceEvent multiPlaceEvent = (BlockEvent.MultiPlaceEvent)event;

            List<BlockState> list = new ArrayList<BlockState>();
            for (BlockSnapshot snap : multiPlaceEvent.getReplacedBlockSnapshots()) {
                BlockPos blockPos = snap.getPos();
                list.add(CraftBlockState.getBlockState(event.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            }

            bukkitEvent = CraftEventFactory.callBlockMultiPlaceEvent(event.getWorld(), event.getPlayer(), event.getHand(), list, clickPos.getX(), clickPos.getY(), clickPos.getZ());
        } else {
            BlockPos blockPos = event.getPos();
            CraftBlockState blockstate = CraftBlockState.getBlockState(event.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
            bukkitEvent = CraftEventFactory.callBlockPlaceEvent(event.getWorld(), event.getPlayer(), event.getHand(), blockstate, clickPos.getX(), clickPos.getY(), clickPos.getZ());
        }

        if (bukkitEvent.isCancelled() || !bukkitEvent.canBuild()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onExplode(ExplosionEvent.Detonate event) {
        if (!CatServer.getConfig().bridgeForgeExplosionEventToBukkit) return;
        Explosion explosion = event.getExplosion();
        if (explosion.getClass() != Explosion.class) {
            Entity exploder = explosion.exploder;
            World bworld = event.getWorld().getWorld();
            Vec3d explosionPos = explosion.getPosition();
            Location location = new Location(bworld, explosionPos.x, explosionPos.y, explosionPos.z);
            List<Block> bukkitBlocks;
            boolean cancelled;
            float yield;
            final List<Block> blockList = Lists.newArrayList();
            List<BlockPos> affectedBlockPositions = event.getAffectedBlocks();
            for (int i1 = affectedBlockPositions.size() - 1; i1 >= 0; i1--) {
                BlockPos cpos = affectedBlockPositions.get(i1);
                Block bblock = bworld.getBlockAt(cpos.getX(), cpos.getY(), cpos.getZ());
                if (bblock.getType() != Material.AIR) {
                    blockList.add(bblock);
                }
            }
            if (exploder != null) {
                EntityExplodeEvent bukkitEvent = new EntityExplodeEvent(exploder.getBukkitEntity(), location , blockList, 1.0F / explosion.size);
                Bukkit.getServer().getPluginManager().callEvent(bukkitEvent);
                cancelled = bukkitEvent.isCancelled();
                bukkitBlocks = bukkitEvent.blockList();
                yield = bukkitEvent.getYield();
            } else {
                BlockExplodeEvent bukkitEvent = new BlockExplodeEvent(location.getBlock(), blockList, 1.0F / explosion.size);
                Bukkit.getServer().getPluginManager().callEvent(bukkitEvent);
                cancelled = bukkitEvent.isCancelled();
                bukkitBlocks = bukkitEvent.blockList();
                yield = bukkitEvent.getYield();
            }
            explosion.getAffectedBlockPositions().clear();

            if (cancelled) {
                event.getAffectedEntities().clear();
                explosion.wasCanceled = true;
            } else {
                for (Block bblock : bukkitBlocks) {
                    BlockPos coords = new BlockPos(bblock.getX(), bblock.getY(), bblock.getZ());
                    explosion.getAffectedBlockPositions().add(coords);
                }
                explosion.size = yield * explosion.size;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isVanilla) {
            org.bukkit.event.player.PlayerInteractEvent bukkitEvent = CraftEventFactory.callPlayerInteractEvent(event.getEntityPlayer(), Action.LEFT_CLICK_BLOCK, event.getPos(), event.getFace(), event.getItemStack(), false, event.getHand());
            if (bukkitEvent.useInteractedBlock() == Event.Result.DENY) {
                event.setCanceled(true);
                event.setCancellationResult(bukkitEvent.useItemInHand() != Event.Result.ALLOW ? EnumActionResult.SUCCESS : EnumActionResult.PASS);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isVanilla) {
            org.bukkit.event.player.PlayerInteractEvent bukkitEvent = CraftEventFactory.callPlayerInteractEvent(event.getEntityPlayer(), Action.RIGHT_CLICK_BLOCK, event.getPos(), event.getFace(), event.getItemStack(), false, event.getHand());
            if (bukkitEvent.useInteractedBlock() == Event.Result.DENY) {
                event.setCanceled(true);
                event.setCancellationResult(bukkitEvent.useItemInHand() != Event.Result.ALLOW ? EnumActionResult.SUCCESS : EnumActionResult.PASS);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Bukkit.getPluginManager().callEvent(new PlayerChangedWorldEvent((CraftPlayer) event.player.getBukkitEntity(), MinecraftServer.getServerInst().getWorldServer(event.fromDim).getWorld()));
    }

    public static class BukkitEventCapture<T extends Event> {
        private T bukkitEvent;

        public boolean hasResult() {
            return bukkitEvent != null;
        }

        public void put(T bukkitEvent) {
            if (this.bukkitEvent == null) {
                this.bukkitEvent = bukkitEvent;
            }
        }

        public T get() {
            T bukkitEvent = this.bukkitEvent;
            this.bukkitEvent = null;
            return bukkitEvent;
        }

        public void reset() {
            bukkitEvent = null;
        }
    }
}
