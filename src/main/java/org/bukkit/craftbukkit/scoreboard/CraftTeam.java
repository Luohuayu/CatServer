// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import org.bukkit.scoreboard.Scoreboard;
import java.util.Iterator;
import org.bukkit.Bukkit;
import com.google.common.collect.ImmutableSet;
import org.bukkit.OfflinePlayer;
import java.util.Set;
import org.bukkit.scoreboard.NameTagVisibility;
import org.apache.commons.lang.Validate;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.bukkit.scoreboard.Team;

final class CraftTeam extends CraftScoreboardComponent implements Team
{
    private final ScorePlayerTeam team;
    
    CraftTeam(final CraftScoreboard scoreboard, final ScorePlayerTeam team) {
        super(scoreboard);
        this.team = team;
    }
    
    @Override
    public String getName() throws IllegalStateException {
        this.checkState();
        return this.team.getRegisteredName();
    }
    
    @Override
    public String getDisplayName() throws IllegalStateException {
        this.checkState();
        return this.team.getTeamName();
    }
    
    @Override
    public void setDisplayName(final String displayName) throws IllegalStateException {
        Validate.notNull((Object)displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name '" + displayName + "' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setTeamName(displayName);
    }
    
    @Override
    public String getPrefix() throws IllegalStateException {
        this.checkState();
        return this.team.getColorPrefix();
    }
    
    @Override
    public void setPrefix(final String prefix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)prefix, "Prefix cannot be null");
        Validate.isTrue(prefix.length() <= 32, "Prefix '" + prefix + "' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setNamePrefix(prefix);
    }
    
    @Override
    public String getSuffix() throws IllegalStateException {
        this.checkState();
        return this.team.getColorSuffix();
    }
    
    @Override
    public void setSuffix(final String suffix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)suffix, "Suffix cannot be null");
        Validate.isTrue(suffix.length() <= 32, "Suffix '" + suffix + "' is longer than the limit of 32 characters");
        this.checkState();
        this.team.setNameSuffix(suffix);
    }
    
    @Override
    public boolean allowFriendlyFire() throws IllegalStateException {
        this.checkState();
        return this.team.getAllowFriendlyFire();
    }
    
    @Override
    public void setAllowFriendlyFire(final boolean enabled) throws IllegalStateException {
        this.checkState();
        this.team.setAllowFriendlyFire(enabled);
    }
    
    @Override
    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        this.checkState();
        return this.team.getSeeFriendlyInvisiblesEnabled();
    }
    
    @Override
    public void setCanSeeFriendlyInvisibles(final boolean enabled) throws IllegalStateException {
        this.checkState();
        this.team.setSeeFriendlyInvisiblesEnabled(enabled);
    }
    
    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        this.checkState();
        return notchToBukkit(this.team.getNameTagVisibility());
    }
    
    @Override
    public void setNameTagVisibility(final NameTagVisibility visibility) throws IllegalArgumentException {
        this.checkState();
        this.team.setNameTagVisibility(bukkitToNotch(visibility));
    }
    
    @Override
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
        this.checkState();
        final ImmutableSet.Builder<OfflinePlayer> players = /*(ImmutableSet.Builder<OfflinePlayer>)*/ImmutableSet.builder();
        for (final String playerName : this.team.getMembershipCollection()) {
            players.add(Bukkit.getOfflinePlayer(playerName));
        }
        return (Set<OfflinePlayer>)players.build();
    }
    
    @Override
    public Set<String> getEntries() throws IllegalStateException {
        this.checkState();
        final ImmutableSet.Builder<String> entries = /*(ImmutableSet.Builder<String>)*/ImmutableSet.builder();
        for (final String playerName : this.team.getMembershipCollection()) {
            entries.add(playerName);
        }
        return (Set<String>)entries.build();
    }
    
    @Override
    public int getSize() throws IllegalStateException {
        this.checkState();
        return this.team.getMembershipCollection().size();
    }
    
    @Override
    public void addPlayer(final OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        this.addEntry(player.getName());
    }
    
    @Override
    public void addEntry(final String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        final CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.addPlayerToTeam(entry, this.team.getRegisteredName());
    }
    
    @Override
    public boolean removePlayer(final OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        return this.removeEntry(player.getName());
    }
    
    @Override
    public boolean removeEntry(final String entry) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull((Object)entry, "Entry cannot be null");
        final CraftScoreboard scoreboard = this.checkState();
        if (!this.team.getMembershipCollection().contains(entry)) {
            return false;
        }
        scoreboard.board.removePlayerFromTeam(entry, this.team);
        return true;
    }
    
    @Override
    public boolean hasPlayer(final OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)player, "OfflinePlayer cannot be null");
        return this.hasEntry(player.getName());
    }
    
    @Override
    public boolean hasEntry(final String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull((Object)"Entry cannot be null");
        this.checkState();
        return this.team.getMembershipCollection().contains(entry);
    }
    
    @Override
    public void unregister() throws IllegalStateException {
        final CraftScoreboard scoreboard = this.checkState();
        scoreboard.board.removeTeam(this.team);
    }
    
    @Override
    public OptionStatus getOption(final Option option) throws IllegalStateException {
        this.checkState();
        switch (option) {
            case NAME_TAG_VISIBILITY: {
                return OptionStatus.values()[this.team.getNameTagVisibility().ordinal()];
            }
            case DEATH_MESSAGE_VISIBILITY: {
                return OptionStatus.values()[this.team.getDeathMessageVisibility().ordinal()];
            }
            case COLLISION_RULE: {
                return OptionStatus.values()[this.team.getCollisionRule().ordinal()];
            }
            default: {
                throw new IllegalArgumentException("Unrecognised option " + option);
            }
        }
    }
    
    @Override
    public void setOption(final Option option, final OptionStatus status) throws IllegalStateException {
        this.checkState();
        switch (option) {
            case NAME_TAG_VISIBILITY: {
                this.team.setNameTagVisibility(net.minecraft.scoreboard.Team.EnumVisible.values()[status.ordinal()]);
                break;
            }
            case DEATH_MESSAGE_VISIBILITY: {
                this.team.setDeathMessageVisibility(net.minecraft.scoreboard.Team.EnumVisible.values()[status.ordinal()]);
                break;
            }
            case COLLISION_RULE: {
                this.team.setCollisionRule(net.minecraft.scoreboard.Team.CollisionRule.values()[status.ordinal()]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unrecognised option " + option);
            }
        }
    }
    
    public static net.minecraft.scoreboard.Team.EnumVisible bukkitToNotch(final NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS: {
                return net.minecraft.scoreboard.Team.EnumVisible.ALWAYS;
            }
            case NEVER: {
                return net.minecraft.scoreboard.Team.EnumVisible.NEVER;
            }
            case HIDE_FOR_OTHER_TEAMS: {
                return net.minecraft.scoreboard.Team.EnumVisible.HIDE_FOR_OTHER_TEAMS;
            }
            case HIDE_FOR_OWN_TEAM: {
                return net.minecraft.scoreboard.Team.EnumVisible.HIDE_FOR_OWN_TEAM;
            }
            default: {
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
            }
        }
    }
    
    public static NameTagVisibility notchToBukkit(final net.minecraft.scoreboard.Team.EnumVisible visibility) {
        switch (visibility) {
            case ALWAYS: {
                return NameTagVisibility.ALWAYS;
            }
            case NEVER: {
                return NameTagVisibility.NEVER;
            }
            case HIDE_FOR_OTHER_TEAMS: {
                return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            }
            case HIDE_FOR_OWN_TEAM: {
                return NameTagVisibility.HIDE_FOR_OWN_TEAM;
            }
            default: {
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
            }
        }
    }
    
    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        if (this.getScoreboard().board.getTeam(this.team.getRegisteredName()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }
        return this.getScoreboard();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + ((this.team != null) ? this.team.hashCode() : 0);
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
        final CraftTeam other = (CraftTeam)obj;
        return this.team == other.team || (this.team != null && this.team.equals(other.team));
    }
}
