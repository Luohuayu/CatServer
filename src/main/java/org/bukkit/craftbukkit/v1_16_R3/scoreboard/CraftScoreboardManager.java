package org.bukkit.craftbukkit.v1_16_R3.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SScoreboardObjectivePacket;
import net.minecraft.network.play.server.STeamsPacket;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<CraftScoreboard>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<CraftPlayer, CraftScoreboard>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.scoreboard.Scoreboard scoreboardServer) {
        mainScoreboard = new CraftScoreboard(scoreboardServer);
        server = minecraftserver;
        scoreboards.add(mainScoreboard);
    }

    @Override
    public CraftScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    @Override
    public CraftScoreboard getNewScoreboard() {
        CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(server));
        scoreboards.add(scoreboard);
        return scoreboard;
    }

    // CraftBukkit method
    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = playerBoards.get(player);
        return (CraftScoreboard) (board == null ? getMainScoreboard() : board);
    }

    // CraftBukkit method
    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) throws IllegalArgumentException {
        Validate.isTrue(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        net.minecraft.scoreboard.Scoreboard oldboard = getPlayerBoard(player).getHandle();
        net.minecraft.scoreboard.Scoreboard newboard = scoreboard.getHandle();
        ServerPlayerEntity entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, (CraftScoreboard) scoreboard);
        }

        // Old objective tracking
        HashSet<ScoreObjective> removed = new HashSet<ScoreObjective>();
        for (int i = 0; i < 3; ++i) {
            ScoreObjective scoreboardobjective = oldboard.getDisplayObjective(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.connection.send(new SScoreboardObjectivePacket(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        Iterator<?> iterator = oldboard.getPlayerTeams().iterator();
        while (iterator.hasNext()) {
            ScorePlayerTeam scoreboardteam = (ScorePlayerTeam) iterator.next();
            entityplayer.connection.send(new STeamsPacket(scoreboardteam, 1));
        }

        // The above is the reverse of the below method.
        server.getPlayerList().updateEntireScoreboard((ServerScoreboard) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(Player player) {
        playerBoards.remove(player);
    }

    // CraftBukkit method
    public void getScoreboardScores(ScoreCriteria criteria, String name, Consumer<Score> consumer) {
        for (CraftScoreboard scoreboard : scoreboards) {
            Scoreboard board = scoreboard.board;
            board.forAllObjectives(criteria, name, (score) -> consumer.accept(score));
        }
    }
}
