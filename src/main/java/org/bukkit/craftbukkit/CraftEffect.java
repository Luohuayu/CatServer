// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import org.bukkit.block.BlockFace;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.potion.Potion;
import org.bukkit.Effect;

public class CraftEffect
{
    public static <T> int getDataValue(final Effect effect, final T data) {
        int datavalue = 0;
        Label_0291: {
            switch (effect) {
                case VILLAGER_PLANT_GROW: {
                    datavalue = (Integer)data;
                    break;
                }
                case POTION_BREAK: {
                    datavalue = (((Potion)data).toDamageValue() & 0x3F);
                    break;
                }
                case RECORD_PLAY: {
                    Validate.isTrue(((Material)data).isRecord(), "Invalid record type!");
                    datavalue = ((Material)data).getId();
                    break;
                }
                case SMOKE: {
                    switch ((BlockFace)data) {
                        case SOUTH_EAST: {
                            datavalue = 0;
                            break Label_0291;
                        }
                        case SOUTH: {
                            datavalue = 1;
                            break Label_0291;
                        }
                        case SOUTH_WEST: {
                            datavalue = 2;
                            break Label_0291;
                        }
                        case EAST: {
                            datavalue = 3;
                            break Label_0291;
                        }
                        case UP:
                        case SELF: {
                            datavalue = 4;
                            break Label_0291;
                        }
                        case WEST: {
                            datavalue = 5;
                            break Label_0291;
                        }
                        case NORTH_EAST: {
                            datavalue = 6;
                            break Label_0291;
                        }
                        case NORTH: {
                            datavalue = 7;
                            break Label_0291;
                        }
                        case NORTH_WEST: {
                            datavalue = 8;
                            break Label_0291;
                        }
                        default: {
                            throw new IllegalArgumentException("Bad smoke direction!");
                        }
                    }
                }
                case STEP_SOUND: {
                    Validate.isTrue(((Material)data).isBlock(), "Material is not a block!");
                    datavalue = ((Material)data).getId();
                    break;
                }
                default: {
                    datavalue = 0;
                    break;
                }
            }
        }
        return datavalue;
    }
}
