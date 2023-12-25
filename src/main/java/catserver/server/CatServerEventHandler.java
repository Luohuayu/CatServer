package catserver.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class CatServerEventHandler {
    public static boolean isDropItems;

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingDropsEvent(LivingDropsEvent event) {
        if (!(event.getEntityLiving() instanceof ServerPlayer)) {
            LivingEntity livingEntity = event.getEntityLiving();
            livingEntity.expToDrop = livingEntity.getExpReward();

            List<org.bukkit.inventory.ItemStack> bukkitDrops = new ArrayList<>();
            for (ItemEntity forgeDrop : event.getDrops()) {
                bukkitDrops.add(CraftItemStack.asCraftMirror(forgeDrop.getItem()));
            }

            // CatServer - handle ArmorStand
            if (livingEntity instanceof net.minecraft.world.entity.decoration.ArmorStand armorStand) {
                bukkitDrops.addAll(armorStand.drops);
                armorStand.drops.clear();
            }
            // CatServer end

            CraftEventFactory.callEntityDeathEvent(livingEntity, bukkitDrops);
            livingEntity.dropExperience();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onBlockPlaceEvent(BlockEvent.EntityPlaceEvent event) {
        BlockPlaceEvent bukkitEvent = null;
        InteractionHand hand = BlockEvent.hand != null ? BlockEvent.hand : InteractionHand.MAIN_HAND;
        BlockPos clickPos = BlockEvent.direction != null ? event.getBlockSnapshot().getPos().relative(BlockEvent.direction.getOpposite()) : event.getPos();

        if (event.getEntity() instanceof ServerPlayer) {
            if (event instanceof BlockEvent.EntityMultiPlaceEvent) {
                BlockEvent.EntityMultiPlaceEvent multiPlaceEvent = (BlockEvent.EntityMultiPlaceEvent) event;

                List<BlockState> list = new ArrayList<>(multiPlaceEvent.getReplacedBlockSnapshots().size());
                for (BlockSnapshot snap : multiPlaceEvent.getReplacedBlockSnapshots()) {
                    BlockPos pos = snap.getPos();
                    list.add(CraftBlockState.getBlockState(event.getWorld(), pos));
                }
                bukkitEvent = CraftEventFactory.callBlockMultiPlaceEvent((ServerLevel) event.getWorld(), (ServerPlayer) event.getEntity(), hand, list, clickPos.getX(), clickPos.getY(), clickPos.getZ());
            } else {
                CraftBlockState blockState = CraftBlockState.getBlockState(event.getWorld(), event.getPos());
                bukkitEvent = CraftEventFactory.callBlockPlaceEvent((ServerLevel) event.getWorld(), (ServerPlayer) event.getEntity(), hand, blockState, clickPos.getX(), clickPos.getY(), clickPos.getZ());
            }

            event.setCanceled(bukkitEvent.isCancelled() || !bukkitEvent.canBuild());
        }

        BlockEvent.direction = null;
        BlockEvent.hand = null;
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onBreakBlockEvent(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            CraftBlock craftBlock = CraftBlock.at(event.getWorld(), event.getPos());
            BlockBreakEvent bukkitEvent = new BlockBreakEvent(craftBlock, ((ServerPlayer) event.getPlayer()).getBukkitEntity());
            bukkitEvent.setCancelled(event.isCanceled());
            bukkitEvent.setExpToDrop(event.getExpToDrop());
            Bukkit.getPluginManager().callEvent(bukkitEvent);
            event.setCanceled(bukkitEvent.isCancelled());
            event.setExpToDrop(bukkitEvent.getExpToDrop());
            isDropItems = bukkitEvent.isDropItems();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerChangeGameModeEvent(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getPlayer();
            PlayerGameModeChangeEvent bukkitEvent = new PlayerGameModeChangeEvent(player.getBukkitEntity(), GameMode.getByValue(event.getNewGameMode().getId()));
            bukkitEvent.setCancelled(event.isCanceled());
            player.level.getCraftServer().getPluginManager().callEvent(bukkitEvent);
            event.setCanceled(bukkitEvent.isCancelled());
        }
    }

    // Not cancelable
    @SubscribeEvent
    public void onAdvancementEvent(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getPlayer();
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }
}
