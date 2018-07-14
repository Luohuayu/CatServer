// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

abstract class CraftScoreboardComponent
{
    private CraftScoreboard scoreboard;
    
    CraftScoreboardComponent(final CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
    
    abstract CraftScoreboard checkState() throws IllegalStateException;
    
    public CraftScoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    abstract void unregister() throws IllegalStateException;
}
