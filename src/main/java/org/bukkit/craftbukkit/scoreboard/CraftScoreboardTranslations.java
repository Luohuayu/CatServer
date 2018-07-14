// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.scoreboard.Scoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import com.google.common.collect.ImmutableBiMap;

class CraftScoreboardTranslations
{
    static final int MAX_DISPLAY_SLOT = 3;
    static ImmutableBiMap<DisplaySlot, String> SLOTS;
    
    static {
        CraftScoreboardTranslations.SLOTS = /*(ImmutableBiMap<DisplaySlot, String>)*/ImmutableBiMap.of(DisplaySlot.BELOW_NAME, "belowName", DisplaySlot.PLAYER_LIST, "list", DisplaySlot.SIDEBAR, "sidebar");
    }
    
    static DisplaySlot toBukkitSlot(final int i) {
        return (DisplaySlot)CraftScoreboardTranslations.SLOTS.inverse().get((Object)Scoreboard.getObjectiveDisplaySlot(i));
    }
    
    static int fromBukkitSlot(final DisplaySlot slot) {
        return Scoreboard.getObjectiveDisplaySlotNumber((String)CraftScoreboardTranslations.SLOTS.get((Object)slot));
    }
}
