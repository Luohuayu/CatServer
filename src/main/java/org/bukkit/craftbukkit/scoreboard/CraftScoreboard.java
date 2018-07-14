// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import java.util.Set;
import org.bukkit.Bukkit;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Score;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import java.util.Iterator;
import com.google.common.collect.ImmutableSet;
import org.bukkit.scoreboard.Objective;
import net.minecraft.scoreboard.ScoreObjective;
import org.apache.commons.lang.Validate;
import org.bukkit.scoreboard.Scoreboard;

public final class CraftScoreboard implements Scoreboard
{
    final net.minecraft.scoreboard.Scoreboard board;
    
    CraftScoreboard(final net.minecraft.scoreboard.Scoreboard board) {
        this.board = board;
    }
    
    @Override
    public CraftObjective registerNewObjective(final String name, final String criteria) throws IllegalArgumentException {
        Validate.notNull((Object)name, "Objective name cannot be null");
        Validate.notNull((Object)criteria, "Criteria cannot be null");
        Validate.isTrue(name.length() <= 16, "The name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getObjective(name) == null, "An objective of name '" + name + "' already exists");
        final CraftCriteria craftCriteria = CraftCriteria.getFromBukkit(criteria);
        final ScoreObjective objective = this.board.addScoreObjective(name, craftCriteria.criteria);
        return new CraftObjective(this, objective);
    }
    
    @Override
    public Objective getObjective(final String name) throws IllegalArgumentException {
        Validate.notNull((Object)name, "Name cannot be null");
        final ScoreObjective nms = this.board.getObjective(name);
        return (nms == null) ? null : new CraftObjective(this, nms);
    }
    
    public ImmutableSet<Objective> getObjectivesByCriteria(final String criteria) throws IllegalArgumentException {
        Validate.notNull((Object)criteria, "Criteria cannot be null");
        final ImmutableSet.Builder<Objective> objectives = /*(ImmutableSet.Builder<Objective>)*/ImmutableSet.builder();
        for (final ScoreObjective netObjective : this.board.getScoreObjectives()) {
            final CraftObjective objective = new CraftObjective(this, netObjective);
            if (objective.getCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }
        return (ImmutableSet<Objective>)objectives.build();
    }
    
    public ImmutableSet<Objective> getObjectives() {
        return (ImmutableSet<Objective>)ImmutableSet.copyOf(Iterables.transform((Iterable)this.board.getScoreObjectives(), (Function)new Function<ScoreObjective, Objective>() {
            public Objective apply(final ScoreObjective input) {
                return new CraftObjective(CraftScoreboard.this, input);
            }
        }));
    }
    
    @Override
    public Objective getObjective(final DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull((Object)slot, "Display slot cannot be null");
        final ScoreObjective objective = this.board.getObjectiveInDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot));
        if (objective == null) {
            return null;
        }
        return new CraftObjective(this, objective);
    }
    
    public ImmutableSet<Score> getScores(final OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        return this.getScores(player.getName());
    }
    
    public ImmutableSet<Score> getScores(final String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        final ImmutableSet.Builder<Score> scores = /*(ImmutableSet.Builder<Score>)*/ImmutableSet.builder();
        for (final ScoreObjective objective : this.board.getScoreObjectives()) {
            scores.add(new CraftScore(new CraftObjective(this, objective), entry));
        }
        return (ImmutableSet<Score>)scores.build();
    }
    
    @Override
    public void resetScores(final OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        this.resetScores(player.getName());
    }
    
    @Override
    public void resetScores(final String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        for (final ScoreObjective objective : this.board.getScoreObjectives()) {
            this.board.removeObjectiveFromEntity(entry, objective);
        }
    }
    
    @Override
    public Team getPlayerTeam(final OfflinePlayer player) throws IllegalArgumentException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        final ScorePlayerTeam team = this.board.getPlayersTeam(player.getName());
        return (team == null) ? null : new CraftTeam(this, team);
    }
    
    @Override
    public Team getEntryTeam(final String entry) throws IllegalArgumentException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        final ScorePlayerTeam team = this.board.getPlayersTeam(entry);
        return (team == null) ? null : new CraftTeam(this, team);
    }
    
    @Override
    public Team getTeam(final String teamName) throws IllegalArgumentException {
        Validate.notNull((Object)teamName, "Team name cannot be null");
        final ScorePlayerTeam team = this.board.getTeam(teamName);
        return (team == null) ? null : new CraftTeam(this, team);
    }
    
    public ImmutableSet<Team> getTeams() {
        return (ImmutableSet<Team>)ImmutableSet.copyOf(Iterables.transform((Iterable)this.board.getTeams(), (Function)new Function<ScorePlayerTeam, Team>() {
            public Team apply(final ScorePlayerTeam input) {
                return new CraftTeam(CraftScoreboard.this, input);
            }
        }));
    }
    
    @Override
    public Team registerNewTeam(final String name) throws IllegalArgumentException {
        Validate.notNull((Object)name, "Team name cannot be null");
        Validate.isTrue(name.length() <= 16, "Team name '" + name + "' is longer than the limit of 16 characters");
        Validate.isTrue(this.board.getTeam(name) == null, "Team name '" + name + "' is already in use");
        return new CraftTeam(this, this.board.createTeam(name));
    }
    
    public ImmutableSet<OfflinePlayer> getPlayers() {
        final ImmutableSet.Builder<OfflinePlayer> players = /*(ImmutableSet.Builder<OfflinePlayer>)*/ImmutableSet.builder();
        for (final Object playerName : this.board.getObjectiveNames()) {
            players.add(Bukkit.getOfflinePlayer(playerName.toString()));
        }
        return (ImmutableSet<OfflinePlayer>)players.build();
    }
    
    public ImmutableSet<String> getEntries() {
        final ImmutableSet.Builder<String> entries = /*(ImmutableSet.Builder<String>)*/ImmutableSet.builder();
        for (final Object entry : this.board.getObjectiveNames()) {
            entries.add(entry.toString());
        }
        return (ImmutableSet<String>)entries.build();
    }
    
    @Override
    public void clearSlot(final DisplaySlot slot) throws IllegalArgumentException {
        Validate.notNull((Object)slot, "Slot cannot be null");
        this.board.setObjectiveInDisplaySlot(CraftScoreboardTranslations.fromBukkitSlot(slot), null);
    }
    
    public net.minecraft.scoreboard.Scoreboard getHandle() {
        return this.board;
    }
}
