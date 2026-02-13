package org.bukkit.craftbukkit.v1_20_R1.block;

import net.minecraft.world.level.block.SupportType;
import org.bukkit.block.BlockSupport;

public final class CraftBlockSupport {

    private CraftBlockSupport() {
    }

    public static BlockSupport toBukkit(SupportType support) {
        return switch (support) {
            case FULL -> BlockSupport.FULL;
            case CENTER -> BlockSupport.CENTER;
            case RIGID -> BlockSupport.RIGID;
            default -> throw new IllegalArgumentException("Unsupported SupportType type: " + support + ". This is a bug.");
        };
    }

    public static SupportType toNMS(BlockSupport support) {
        return switch (support) {
            case FULL -> SupportType.FULL;
            case CENTER -> SupportType.CENTER;
            case RIGID -> SupportType.RIGID;
            default -> throw new IllegalArgumentException("Unsupported BlockSupport type: " + support + ". This is a bug.");
        };
    }
}
