// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.util.Set;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.HashSet;
import net.minecraft.server.MinecraftServer;
import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player>
{
    private final MinecraftServer server;
    
    public LazyPlayerSet(final MinecraftServer server) {
        this.server = server;
    }
    
    @Override
    HashSet<Player> makeReference() {
        if (this.reference != null) {
            throw new IllegalStateException("Reference already created!");
        }
        final List<EntityPlayerMP> players = this.server.getPlayerList().playerEntityList;
        final HashSet<Player> reference = new HashSet<Player>(players.size());
        for (final EntityPlayerMP player : players) {
            reference.add(player.getBukkitEntity());
        }
        return reference;
    }
}
