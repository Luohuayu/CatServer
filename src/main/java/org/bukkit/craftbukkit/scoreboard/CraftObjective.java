// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import org.bukkit.scoreboard.Score;
import org.bukkit.OfflinePlayer;
import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.apache.commons.lang.Validate;
import net.minecraft.scoreboard.ScoreObjective;
import org.bukkit.scoreboard.Objective;

final class CraftObjective extends CraftScoreboardComponent implements Objective
{
    private final ScoreObjective objective;
    private final CraftCriteria criteria;
    
    CraftObjective(final CraftScoreboard scoreboard, final ScoreObjective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = CraftCriteria.getFromNMS(objective);
    }
    
    ScoreObjective getHandle() {
        return this.objective;
    }
    
    @Override
    public String getName() throws IllegalStateException {
        this.checkState();
        return this.objective.getName();
    }
    
    @Override
    public String getDisplayName() throws IllegalStateException {
        this.checkState();
        return this.objective.getDisplayName();
    }
    
    @Override
    public void setDisplayName(final String displayName) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name '" + displayName + "' is longer than the limit of 32 characters");
        this.checkState();
        this.objective.setDisplayName(displayName);
    }
    
    @Override
    public String getCriteria() throws IllegalStateException {
        this.checkState();
        return this.criteria.bukkitName;
    }
    
    @Override
    public boolean isModifiable() throws IllegalStateException {
        this.checkState();
        return !this.criteria.criteria.isReadOnly();
    }
    
    @Override
    public void setDisplaySlot(final DisplaySlot slot) throws IllegalStateException {
        final CraftScoreboard scoreboard = this.checkState();
        final Scoreboard board = scoreboard.board;
        final ScoreObjective objective = this.objective;
        for (int i = 0; i < 3; ++i) {
            if (board.getObjectiveInDisplaySlot(i) == objective) {
                board.setObjectiveInDisplaySlot(i, null);
            }
        }
        if (slot != null) {
            final int slotNumber = CraftScoreboardTranslations.fromBukkitSlot(slot);
            board.setObjectiveInDisplaySlot(slotNumber, this.getHandle());
        }
    }
    
    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        final CraftScoreboard scoreboard = this.checkState();
        final Scoreboard board = scoreboard.board;
        final ScoreObjective objective = this.objective;
        for (int i = 0; i < 3; ++i) {
            if (board.getObjectiveInDisplaySlot(i) == objective) {
                return CraftScoreboardTranslations.toBukkitSlot(i);
            }
        }
        return null;
    }
    
    @Override
    public Score getScore(final OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)player, "Player cannot be null");
        this.checkState();
        return new CraftScore(this, player.getName());
    }
    
    @Override
    public Score getScore(final String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        this.checkState();
        return new CraftScore(this, entry);
    }
    
    @Override
    public void unregister() throws IllegalStateException {
        final CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.removeObjective(this.objective);
    }
    
    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.getObjective(this.objective.getName()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }
        return this.getScoreboard();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + ((this.objective != null) ? this.objective.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftObjective other = (CraftObjective)obj;
        return this.objective == other.objective || (this.objective != null && this.objective.equals(other.objective));
    }
}
