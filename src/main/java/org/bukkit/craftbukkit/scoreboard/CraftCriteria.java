package org.bukkit.craftbukkit.scoreboard;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

final class CraftCriteria {
    static final Map<String, CraftCriteria> DEFAULTS;
    static final CraftCriteria DUMMY;

    static {
        ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();

        for (Map.Entry<?, ?> entry : ((Map<?,?> ) IScoreCriteria.INSTANCES).entrySet()) {
            String name = entry.getKey().toString();
            IScoreCriteria criteria = (IScoreCriteria) entry.getValue();

            defaults.put(name, new CraftCriteria(criteria));
        }

        DEFAULTS = defaults.build();
        DUMMY = DEFAULTS.get("dummy");
    }

    final IScoreCriteria criteria;
    final String bukkitName;

    private CraftCriteria(String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = DUMMY.criteria;
    }

    private CraftCriteria(IScoreCriteria criteria) {
        this.criteria = criteria;
        this.bukkitName = criteria.getName();
    }

    static CraftCriteria getFromNMS(ScoreObjective objective) {
        return DEFAULTS.get(objective.getCriteria().getName());
    }

    static CraftCriteria getFromBukkit(String name) {
        final CraftCriteria criteria = DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }
        return new CraftCriteria(name);
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof CraftCriteria)) {
            return false;
        }
        return ((CraftCriteria) that).bukkitName.equals(this.bukkitName);
    }

    @Override
    public int hashCode() {
        return this.bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
    }
}
