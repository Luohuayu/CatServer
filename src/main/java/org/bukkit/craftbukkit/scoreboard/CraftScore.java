// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.scoreboard.ScoreObjective;
import java.util.Map;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Score;

final class CraftScore implements Score
{
    private final String entry;
    private final CraftObjective objective;
    
    CraftScore(final CraftObjective objective, final String entry) {
        this.objective = objective;
        this.entry = entry;
    }
    
    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.entry);
    }
    
    @Override
    public String getEntry() {
        return this.entry;
    }
    
    @Override
    public Objective getObjective() {
        return this.objective;
    }
    
    @Override
    public int getScore() throws IllegalStateException {
        final Scoreboard board = this.objective.checkState().board;
        if (board.getObjectiveNames().contains(this.entry)) {
            final Map<ScoreObjective, net.minecraft.scoreboard.Score> scores = board.getObjectivesForEntity(this.entry);
            final net.minecraft.scoreboard.Score score = scores.get(this.objective.getHandle());
            if (score != null) {
                return score.getScorePoints();
            }
        }
        return 0;
    }
    
    @Override
    public void setScore(final int score) throws IllegalStateException {
        this.objective.checkState().board.getOrCreateScore(this.entry, this.objective.getHandle()).setScorePoints(score);
    }
    
    @Override
    public CraftScoreboard getScoreboard() {
        return this.objective.getScoreboard();
    }
}
