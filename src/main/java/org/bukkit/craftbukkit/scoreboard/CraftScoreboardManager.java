// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.IScoreCriteria;
import org.bukkit.entity.Player;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.scoreboard.ScoreObjective;
import java.util.HashSet;
import org.apache.commons.lang.Validate;
import net.minecraft.scoreboard.ServerScoreboard;
import java.util.HashMap;
import org.bukkit.craftbukkit.util.WeakCollection;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import java.util.Map;
import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager
{
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards;
    private final Map<CraftPlayer, CraftScoreboard> playerBoards;
    
    public CraftScoreboardManager(final MinecraftServer minecraftserver, final Scoreboard scoreboardServer) {
        this.scoreboards = new WeakCollection<CraftScoreboard>();
        this.playerBoards = new HashMap<CraftPlayer, CraftScoreboard>();
        this.mainScoreboard = new CraftScoreboard(scoreboardServer);
        this.server = minecraftserver;
        this.scoreboards.add(this.mainScoreboard);
    }
    
    @Override
    public CraftScoreboard getMainScoreboard() {
        return this.mainScoreboard;
    }
    
    @Override
    public CraftScoreboard getNewScoreboard() {
        final CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(this.server));
        this.scoreboards.add(scoreboard);
        return scoreboard;
    }
    
    public CraftScoreboard getPlayerBoard(final CraftPlayer player) {
        final CraftScoreboard board = this.playerBoards.get(player);
        return (board == null) ? this.getMainScoreboard() : board;
    }
    
    public void setPlayerBoard(final CraftPlayer player, final org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");
        final CraftScoreboard scoreboard = (CraftScoreboard)bukkitScoreboard;
        final Scoreboard oldboard = this.getPlayerBoard(player).getHandle();
        final Scoreboard newboard = scoreboard.getHandle();
        final EntityPlayerMP entityplayer = player.getHandle();
        if (oldboard == newboard) {
            return;
        }
        if (scoreboard == this.mainScoreboard) {
            this.playerBoards.remove(player);
        }
        else {
            this.playerBoards.put(player, scoreboard);
        }
        final HashSet<ScoreObjective> removed = new HashSet<ScoreObjective>();
        for (int i = 0; i < 3; ++i) {
            final ScoreObjective scoreboardobjective = oldboard.getObjectiveInDisplaySlot(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.connection.sendPacket(new SPacketScoreboardObjective(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }
        for (final ScorePlayerTeam scoreboardteam : oldboard.getTeams()) {
            entityplayer.connection.sendPacket(new SPacketTeams(scoreboardteam, 1));
        }
        this.server.getPlayerList().sendScoreboard((ServerScoreboard)newboard, player.getHandle());
    }
    
    public void removePlayer(final Player player) {
        this.playerBoards.remove(player);
    }
    
    public Collection<Score> getScoreboardScores(final IScoreCriteria criteria, final String name, final Collection<Score> collection) {
        for (final CraftScoreboard scoreboard : this.scoreboards) {
            final Scoreboard board = scoreboard.board;
            for (final ScoreObjective objective : board.getObjectivesFromCriteria(criteria)) {
                collection.add(board.getOrCreateScore(name, objective));
            }
        }
        return collection;
    }
}
