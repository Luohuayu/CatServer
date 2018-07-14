// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.scoreboard;

import net.minecraft.scoreboard.ScoreObjective;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import net.minecraft.scoreboard.IScoreCriteria;
import java.util.Map;

final class CraftCriteria
{
    static final Map<String, CraftCriteria> DEFAULTS;
    static final CraftCriteria DUMMY;
    final IScoreCriteria criteria;
    final String bukkitName;
    
    static {
        final ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();
        for (final Map.Entry<?, ?> entry : IScoreCriteria.INSTANCES.entrySet()) {
            final String name = entry.getKey().toString();
            final IScoreCriteria criteria = (IScoreCriteria)entry.getValue();
            defaults.put(name, new CraftCriteria(criteria));
        }
        DEFAULTS = (Map)defaults.build();
        DUMMY = CraftCriteria.DEFAULTS.get("dummy");
    }
    
    private CraftCriteria(final String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = CraftCriteria.DUMMY.criteria;
    }
    
    private CraftCriteria(final IScoreCriteria criteria) {
        this.criteria = criteria;
        this.bukkitName = criteria.getName();
    }
    
    static CraftCriteria getFromNMS(final ScoreObjective objective) {
        return CraftCriteria.DEFAULTS.get(objective.getCriteria().getName());
    }
    
    static CraftCriteria getFromBukkit(final String name) {
        final CraftCriteria criteria = CraftCriteria.DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }
        return new CraftCriteria(name);
    }
    
    @Override
    public boolean equals(final Object that) {
        return that instanceof CraftCriteria && ((CraftCriteria)that).bukkitName.equals(this.bukkitName);
    }
    
    @Override
    public int hashCode() {
        return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
    }
}
