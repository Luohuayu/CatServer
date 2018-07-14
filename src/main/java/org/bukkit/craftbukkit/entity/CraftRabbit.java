// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.entity;

import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAITasks;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Rabbit;

public class CraftRabbit extends CraftAnimals implements Rabbit
{
    public CraftRabbit(final CraftServer server, final EntityRabbit entity) {
        super(server, entity);
    }
    
    @Override
    public EntityRabbit getHandle() {
        return (EntityRabbit)this.entity;
    }
    
    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + this.getRabbitType() + "}";
    }
    
    @Override
    public EntityType getType() {
        return EntityType.RABBIT;
    }
    
    @Override
    public Type getRabbitType() {
        final int type = this.getHandle().getRabbitType();
        return CraftMagicMapping.fromMagic(type);
    }
    
    @Override
    public void setRabbitType(final Type type) {
        final EntityRabbit entity = this.getHandle();
        if (this.getRabbitType() == Type.THE_KILLER_BUNNY) {
            final World world = ((CraftWorld)this.getWorld()).getHandle();
            entity.tasks = new EntityAITasks((world != null && world.theProfiler != null) ? world.theProfiler : null);
            entity.targetTasks = new EntityAITasks((world != null && world.theProfiler != null) ? world.theProfiler : null);
            entity.initializePathFinderGoals();
        }
        entity.setRabbitType(CraftMagicMapping.toMagic(type));
    }
    
    private static class CraftMagicMapping
    {
        private static final int[] types;
        private static final Type[] reverse;
        
        static {
            types = new int[Type.values().length];
            reverse = new Type[Type.values().length];
            set(Type.BROWN, 0);
            set(Type.WHITE, 1);
            set(Type.BLACK, 2);
            set(Type.BLACK_AND_WHITE, 3);
            set(Type.GOLD, 4);
            set(Type.SALT_AND_PEPPER, 5);
            set(Type.THE_KILLER_BUNNY, 99);
        }
        
        private static void set(final Type type, final int value) {
            CraftMagicMapping.types[type.ordinal()] = value;
            if (value < CraftMagicMapping.reverse.length) {
                CraftMagicMapping.reverse[value] = type;
            }
        }
        
        public static Type fromMagic(final int magic) {
            if (magic >= 0 && magic < CraftMagicMapping.reverse.length) {
                return CraftMagicMapping.reverse[magic];
            }
            if (magic == 99) {
                return Type.THE_KILLER_BUNNY;
            }
            return null;
        }
        
        public static int toMagic(final Type type) {
            return CraftMagicMapping.types[type.ordinal()];
        }
    }
}
