package catserver.server;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_16_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_16_R3.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class CatServerEventHandler {
    public static boolean isDropItems;

    @SubscribeEvent(receiveCanceled = true)
    public void onBreakBlockEvent(BlockEvent.BreakEvent event) {
        if (!event.getWorld().isClientSide()) {
            CraftBlock craftBlock = CraftBlock.at(event.getWorld(), event.getPos());
            BlockBreakEvent bukkitEvent = new BlockBreakEvent(craftBlock, ((ServerPlayerEntity) event.getPlayer()).getBukkitEntity());
            bukkitEvent.setCancelled(event.isCanceled());
            bukkitEvent.setExpToDrop(event.getExpToDrop());
            Bukkit.getPluginManager().callEvent(bukkitEvent);
            event.setCanceled(bukkitEvent.isCancelled());
            event.setExpToDrop(bukkitEvent.getExpToDrop());
            isDropItems = bukkitEvent.isDropItems();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {
        Entity entity = event.getEntity();
        World world = event.getWorld() instanceof World ? (World)event.getWorld() : entity.level;
        if (world == null) return;;

        if (event.getEntity() instanceof ServerPlayerEntity) {
            event.setCanceled(CraftEventFactory.callPlayerInteractEvent((PlayerEntity) entity, org.bukkit.event.block.Action.PHYSICAL, event.getPos(), null, null, event.isCanceled(),null).useInteractedBlock() == Event.Result.DENY);
        } else {
            EntityInteractEvent bukkitEvent = new EntityInteractEvent(event.getEntity().getBukkitEntity(), entity.level.getWorld().getBlockAt(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()));
            bukkitEvent.setCancelled(event.isCanceled());
            world.getCBServer().getPluginManager().callEvent(bukkitEvent);
            event.setCanceled(bukkitEvent.isCancelled());

            event.setCanceled(CraftEventFactory.callEntityChangeBlockEvent(entity, event.getPos(), Blocks.DIRT.defaultBlockState(), event.isCanceled()).isCancelled());
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerChangeGameModeEvent(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            PlayerGameModeChangeEvent bukkitEvent = new PlayerGameModeChangeEvent(player.getBukkitEntity(), GameMode.getByValue(event.getNewGameMode().getId()));
            bukkitEvent.setCancelled(event.isCanceled());
            player.level.getCBServer().getPluginManager().callEvent(bukkitEvent);
            event.setCanceled(bukkitEvent.isCancelled());
        }
    }

    // Not cancelable
    @SubscribeEvent
    public void onAdvancementEvent(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            Bukkit.getPluginManager().callEvent(new PlayerAdvancementDoneEvent(player.getBukkitEntity(), event.getAdvancement().bukkit));
        }
    }
}
