package org.bukkit.craftbukkit.v1_16_R3.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Spellcaster;

public class CraftSpellcaster extends CraftIllager implements Spellcaster {

    public CraftSpellcaster(CraftServer server, SpellcastingIllagerEntity entity) {
        super(server, entity);
    }

    @Override
    public SpellcastingIllagerEntity getHandle() {
        return (SpellcastingIllagerEntity) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSpellcaster";
    }

    @Override
    public Spell getSpell() {
        return toBukkitSpell(getHandle().getCurrentSpell());
    }

    @Override
    public void setSpell(Spell spell) {
        Preconditions.checkArgument(spell != null, "Use Spell.NONE");

        getHandle().setIsCastingSpell(toNMSSpell(spell));
    }

    public static Spell toBukkitSpell(SpellcastingIllagerEntity.SpellType spell) {
        return Spell.valueOf(spell.name());
    }

    public static SpellcastingIllagerEntity.SpellType toNMSSpell(Spell spell) {
        return SpellcastingIllagerEntity.SpellType.byId(spell.ordinal());
    }
}
