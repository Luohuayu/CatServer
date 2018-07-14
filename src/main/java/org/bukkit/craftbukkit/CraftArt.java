// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.Art;
import net.minecraft.entity.item.EntityPainting;

public class CraftArt
{
    public static Art NotchToBukkit(final EntityPainting.EnumArt art) {
        switch (art) {
            case KEBAB: {
                return Art.KEBAB;
            }
            case AZTEC: {
                return Art.AZTEC;
            }
            case ALBAN: {
                return Art.ALBAN;
            }
            case AZTEC_2: {
                return Art.AZTEC2;
            }
            case BOMB: {
                return Art.BOMB;
            }
            case PLANT: {
                return Art.PLANT;
            }
            case WASTELAND: {
                return Art.WASTELAND;
            }
            case POOL: {
                return Art.POOL;
            }
            case COURBET: {
                return Art.COURBET;
            }
            case SEA: {
                return Art.SEA;
            }
            case SUNSET: {
                return Art.SUNSET;
            }
            case CREEBET: {
                return Art.CREEBET;
            }
            case WANDERER: {
                return Art.WANDERER;
            }
            case GRAHAM: {
                return Art.GRAHAM;
            }
            case MATCH: {
                return Art.MATCH;
            }
            case BUST: {
                return Art.BUST;
            }
            case STAGE: {
                return Art.STAGE;
            }
            case VOID: {
                return Art.VOID;
            }
            case SKULL_AND_ROSES: {
                return Art.SKULL_AND_ROSES;
            }
            case FIGHTERS: {
                return Art.FIGHTERS;
            }
            case POINTER: {
                return Art.POINTER;
            }
            case PIGSCENE: {
                return Art.PIGSCENE;
            }
            case BURNING_SKULL: {
                return Art.BURNINGSKULL;
            }
            case SKELETON: {
                return Art.SKELETON;
            }
            case DONKEY_KONG: {
                return Art.DONKEYKONG;
            }
            case WITHER: {
                return Art.WITHER;
            }
            default: {
                throw new AssertionError(art);
            }
        }
    }
    
    public static EntityPainting.EnumArt BukkitToNotch(final Art art) {
        switch (art) {
            case KEBAB: {
                return EntityPainting.EnumArt.KEBAB;
            }
            case AZTEC: {
                return EntityPainting.EnumArt.AZTEC;
            }
            case ALBAN: {
                return EntityPainting.EnumArt.ALBAN;
            }
            case AZTEC2: {
                return EntityPainting.EnumArt.AZTEC_2;
            }
            case BOMB: {
                return EntityPainting.EnumArt.BOMB;
            }
            case PLANT: {
                return EntityPainting.EnumArt.PLANT;
            }
            case WASTELAND: {
                return EntityPainting.EnumArt.WASTELAND;
            }
            case POOL: {
                return EntityPainting.EnumArt.POOL;
            }
            case COURBET: {
                return EntityPainting.EnumArt.COURBET;
            }
            case SEA: {
                return EntityPainting.EnumArt.SEA;
            }
            case SUNSET: {
                return EntityPainting.EnumArt.SUNSET;
            }
            case CREEBET: {
                return EntityPainting.EnumArt.CREEBET;
            }
            case WANDERER: {
                return EntityPainting.EnumArt.WANDERER;
            }
            case GRAHAM: {
                return EntityPainting.EnumArt.GRAHAM;
            }
            case MATCH: {
                return EntityPainting.EnumArt.MATCH;
            }
            case BUST: {
                return EntityPainting.EnumArt.BUST;
            }
            case STAGE: {
                return EntityPainting.EnumArt.STAGE;
            }
            case VOID: {
                return EntityPainting.EnumArt.VOID;
            }
            case SKULL_AND_ROSES: {
                return EntityPainting.EnumArt.SKULL_AND_ROSES;
            }
            case FIGHTERS: {
                return EntityPainting.EnumArt.FIGHTERS;
            }
            case POINTER: {
                return EntityPainting.EnumArt.POINTER;
            }
            case PIGSCENE: {
                return EntityPainting.EnumArt.PIGSCENE;
            }
            case BURNINGSKULL: {
                return EntityPainting.EnumArt.BURNING_SKULL;
            }
            case SKELETON: {
                return EntityPainting.EnumArt.SKELETON;
            }
            case DONKEYKONG: {
                return EntityPainting.EnumArt.DONKEY_KONG;
            }
            case WITHER: {
                return EntityPainting.EnumArt.WITHER;
            }
            default: {
                throw new AssertionError(art);
            }
        }
    }
}
