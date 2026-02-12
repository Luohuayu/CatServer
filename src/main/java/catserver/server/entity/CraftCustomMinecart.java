package catserver.server.entity;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftMinecart;
import org.bukkit.entity.EntityType;

public class CraftCustomMinecart extends CraftMinecart {

    public CraftCustomMinecart(CraftServer server, AbstractMinecart entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCustomMinecart";
    }

    @Override
    public EntityType getType() {
        return EntityType.MINECART;
    }
}
