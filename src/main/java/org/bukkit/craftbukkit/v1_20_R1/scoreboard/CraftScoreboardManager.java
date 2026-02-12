package org.bukkit.craftbukkit.v1_20_R1.scoreboard;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.util.WeakCollection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

public final class CraftScoreboardManager implements ScoreboardManager {
    private final CraftScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<CraftScoreboard> scoreboards = new WeakCollection<>();
    private final Map<CraftPlayer, CraftScoreboard> playerBoards = new HashMap<>();

    public CraftScoreboardManager(MinecraftServer minecraftserver, net.minecraft.world.scores.Scoreboard scoreboardServer) {
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
        // org.spigotmc.AsyncCatcher.catchOp("scoreboard creation"); // Spigot // CatServer - remove
        CraftScoreboard scoreboard = new CraftScoreboard(new ServerScoreboard(server));
        scoreboards.add(scoreboard);
        return scoreboard;
    }

    // CraftBukkit method
    public CraftScoreboard getPlayerBoard(CraftPlayer player) {
        CraftScoreboard board = playerBoards.get(player);
        return board == null ? getMainScoreboard() : board;
    }

    // CraftBukkit method
    public void setPlayerBoard(CraftPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) {
        Preconditions.checkArgument(bukkitScoreboard instanceof CraftScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        CraftScoreboard scoreboard = (CraftScoreboard) bukkitScoreboard;
        net.minecraft.world.scores.Scoreboard oldboard = getPlayerBoard(player).getHandle();
        net.minecraft.world.scores.Scoreboard newboard = scoreboard.getHandle();
        ServerPlayer entityplayer = player.getHandle();

        if (oldboard == newboard) {
            return;
        }

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, scoreboard);
        }

        // Old objective tracking
        HashSet<Objective> removed = new HashSet<>();
        for (int i = 0; i < 3; ++i) {
            Objective scoreboardobjective = oldboard.getDisplayObjective(i);
            if (scoreboardobjective != null && !removed.contains(scoreboardobjective)) {
                entityplayer.connection.send(new ClientboundSetObjectivePacket(scoreboardobjective, 1));
                removed.add(scoreboardobjective);
            }
        }

        // Old team tracking
        Iterator<?> iterator = oldboard.getPlayerTeams().iterator();
        while (iterator.hasNext()) {
            PlayerTeam scoreboardteam = (PlayerTeam) iterator.next();
            entityplayer.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(scoreboardteam));
        }

        // The above is the reverse of the below method.
        server.getPlayerList().updateEntireScoreboard((ServerScoreboard) newboard, player.getHandle());
    }

    // CraftBukkit method
    public void removePlayer(Player player) {
        playerBoards.remove(player);
    }

    // CraftBukkit method
    public void getScoreboardScores(ObjectiveCriteria criteria, String name, Consumer<Score> consumer) {
        for (CraftScoreboard scoreboard : scoreboards) {
            Scoreboard board = scoreboard.board;
            board.forAllObjectives(criteria, name, (score) -> consumer.accept(score));
        }
    }
}
