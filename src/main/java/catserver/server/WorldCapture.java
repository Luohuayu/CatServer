package catserver.server;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class WorldCapture {
    private WorldServer world;
    private boolean capture;

    private EntityPlayer curPlayer;
    private ItemStack curItemStack;
    private EnumHand curHand;

    private List<EntitySnap> entitySnap = Lists.newArrayList();
    private List<ItemSnap> itemSnap = Lists.newArrayList();

    private static Class<?>[] excludeEntities = new Class<?>[] {EntityPlayer.class, EntityEnderCrystal.class};

    public WorldCapture(WorldServer world) {
        this.world = world;
    }

    public void startCapture(EntityPlayer player, ItemStack stack, EnumHand hand) {
        this.curPlayer = player;
        this.curItemStack = stack;
        this.curHand = hand;

        this.entitySnap.clear();
        this.itemSnap.clear();

        this.capture = true;
    }

    public void stopCapture() {
        this.capture = false;
    }

    public boolean isCapture() {
        return this.capture && world.captureBlockSnapshots;
    }

    public void apply() {
        for (EntitySnap snap : entitySnap) {
            snap.apply();
        }

        for (ItemSnap snap : itemSnap) {
            snap.apply();
        }
    }

    public void restore() {
        curPlayer.setHeldItem(curHand, curItemStack);
    }

    public void addEntitySnap(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        if (this.world.restoringBlockSnapshots) return;
        entitySnap.add(new EntitySnap(entity, reason));
    }

    public void addItemSnap(EntityPlayer player, ItemStack stack) {
        if (this.world.restoringBlockSnapshots) return;
        itemSnap.add(new ItemSnap(player, stack));
    }

    class EntitySnap {
        private final Entity entity;
        private final CreatureSpawnEvent.SpawnReason reason;

        private boolean isApply;

        public EntitySnap(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
            this.entity = entity;
            this.reason = reason;
        }

        public void apply() {
            if (!isApply) {
                world.addEntity(entity, reason);
                isApply = true;
            }
        }
    }

    class ItemSnap {
        private final EntityPlayer player;
        private final ItemStack stack;

        private boolean isApply;

        public ItemSnap(EntityPlayer player, ItemStack stack) {
            this.player = player;
            this.stack = stack;
        }

        public void apply() {
            if (!isApply) {
                if (player.inventory.addItemStackToInventory(stack))
                    player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, stack));
                isApply = true;
            }
        }
    }

    public static boolean canCapture(Entity entity) {
        for (Class<?> excludeEntity : excludeEntities) {
            if (excludeEntity.isAssignableFrom(entity.getClass()))
                return false;
        }
        return true;
    }
}
