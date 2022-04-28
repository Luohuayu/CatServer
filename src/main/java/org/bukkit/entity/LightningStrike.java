package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an instance of a lightning strike. May or may not do damage.
 */
public interface LightningStrike extends Entity {

    /**
     * Returns whether the strike is an effect that does no damage.
     *
     * @return whether the strike is an effect
     */
    public boolean isEffect();

    // Spigot start
    public class Spigot extends Entity.Spigot {

        /*
         * Returns whether the strike is silent.
         *
         * @return whether the strike is silent.
         */
        public boolean isSilent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @NotNull
    @Override
    Spigot spigot();
    // Spigot end
}
