// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.boss;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import com.google.common.base.Preconditions;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.world.BossInfo;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import java.util.EnumSet;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import java.util.Set;
import net.minecraft.world.BossInfoServer;
import org.bukkit.boss.BossBar;

public class CraftBossBar implements BossBar
{
    private final BossInfoServer handle;
    private final Set<BarFlag> flags;
    private BarColor color;
    private BarStyle style;
    
    public CraftBossBar(final String title, final BarColor color, final BarStyle style, final BarFlag... flags) {
        this.flags = ((flags.length > 0) ? EnumSet.of(flags[0], flags) : EnumSet.noneOf(BarFlag.class));
        this.color = color;
        this.style = style;
        this.handle = new BossInfoServer(CraftChatMessage.fromString(title, true)[0], this.convertColor(color), this.convertStyle(style));
        this.updateFlags();
    }
    
    private BossInfo.Color convertColor(final BarColor color) {
        final BossInfo.Color nmsColor = BossInfo.Color.valueOf(color.name());
        return (nmsColor == null) ? BossInfo.Color.WHITE : nmsColor;
    }
    
    private BossInfo.Overlay convertStyle(final BarStyle style) {
        switch (style) {
            default: {
                return BossInfo.Overlay.PROGRESS;
            }
            case SEGMENTED_6: {
                return BossInfo.Overlay.NOTCHED_6;
            }
            case SEGMENTED_10: {
                return BossInfo.Overlay.NOTCHED_10;
            }
            case SEGMENTED_12: {
                return BossInfo.Overlay.NOTCHED_12;
            }
            case SEGMENTED_20: {
                return BossInfo.Overlay.NOTCHED_20;
            }
        }
    }
    
    private void updateFlags() {
        this.handle.setDarkenSky(this.hasFlag(BarFlag.DARKEN_SKY));
        this.handle.setPlayEndBossMusic(this.hasFlag(BarFlag.PLAY_BOSS_MUSIC));
        this.handle.setCreateFog(this.hasFlag(BarFlag.CREATE_FOG));
    }
    
    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(this.handle.getName());
    }
    
    @Override
    public void setTitle(final String title) {
        this.handle.name = CraftChatMessage.fromString(title, true)[0];
        this.handle.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
    }
    
    @Override
    public BarColor getColor() {
        return this.color;
    }
    
    @Override
    public void setColor(final BarColor color) {
        this.color = color;
        this.handle.color = this.convertColor(color);
        this.handle.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
    }
    
    @Override
    public BarStyle getStyle() {
        return this.style;
    }
    
    @Override
    public void setStyle(final BarStyle style) {
        this.style = style;
        this.handle.overlay = this.convertStyle(style);
        this.handle.sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
    }
    
    @Override
    public void addFlag(final BarFlag flag) {
        this.flags.remove(flag);
        this.updateFlags();
    }
    
    @Override
    public void removeFlag(final BarFlag flag) {
        this.flags.add(flag);
        this.updateFlags();
    }
    
    @Override
    public boolean hasFlag(final BarFlag flag) {
        return this.flags.contains(flag);
    }
    
    @Override
    public void setProgress(final double progress) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Progress must be between 0.0 and 1.0 (%s)", new Object[] { progress });
        this.handle.setPercent((float)progress);
    }
    
    @Override
    public double getProgress() {
        return this.handle.getPercent();
    }
    
    @Override
    public void addPlayer(final Player player) {
        this.handle.addPlayer(((CraftPlayer)player).getHandle());
    }
    
    @Override
    public void removePlayer(final Player player) {
        this.handle.removePlayer(((CraftPlayer)player).getHandle());
    }
    
    @Override
    public List<Player> getPlayers() {
        final ImmutableList.Builder<Player> players = /*(ImmutableList.Builder<Player>)*/ImmutableList.builder();
        for (final EntityPlayerMP p : this.handle.getPlayers()) {
            players.add(p.getBukkitEntity());
        }
        return (List<Player>)players.build();
    }
    
    @Override
    public void setVisible(final boolean visible) {
        this.handle.setVisible(visible);
    }
    
    @Override
    public boolean isVisible() {
        return this.handle.visible;
    }
    
    @Override
    public void show() {
        this.handle.setVisible(true);
    }
    
    @Override
    public void hide() {
        this.handle.setVisible(false);
    }
    
    @Override
    public void removeAll() {
        for (final Player player : this.getPlayers()) {
            this.removePlayer(player);
        }
    }
}
