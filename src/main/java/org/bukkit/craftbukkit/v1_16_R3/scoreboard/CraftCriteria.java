package org.bukkit.craftbukkit.v1_16_R3.scoreboard;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;

final class CraftCriteria {
    static final Map<String, CraftCriteria> DEFAULTS;
    static final CraftCriteria DUMMY;

    static {
        ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();

        for (Map.Entry<?, ?> entry : ((Map<?,?> ) ScoreCriteria.CRITERIA_BY_NAME).entrySet()) {
            String name = entry.getKey().toString();
            ScoreCriteria criteria = (ScoreCriteria) entry.getValue();

            defaults.put(name, new CraftCriteria(criteria));
        }

        DEFAULTS = defaults.build();
        DUMMY = DEFAULTS.get("dummy");
    }

    final ScoreCriteria criteria;
    final String bukkitName;

    private CraftCriteria(String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = DUMMY.criteria;
    }

    private CraftCriteria(ScoreCriteria criteria) {
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
