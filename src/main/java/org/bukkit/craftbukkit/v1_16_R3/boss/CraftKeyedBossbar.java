package org.bukkit.craftbukkit.v1_16_R3.boss;

import net.minecraft.server.CustomServerBossInfo;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;

public class CraftKeyedBossbar extends CraftBossBar implements KeyedBossBar {

    public CraftKeyedBossbar(CustomServerBossInfo bossBattleCustom) {
        super(bossBattleCustom);
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(getHandle().getTextId());
    }

    @Override
    public CustomServerBossInfo getHandle() {
        return (CustomServerBossInfo) super.getHandle();
    }
}
