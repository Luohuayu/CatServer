// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.map;

import java.util.Iterator;
import org.bukkit.map.MapCursorCollection;
import net.minecraft.util.math.Vec4b;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;
import net.minecraft.world.storage.MapData;
import org.bukkit.map.MapRenderer;

public class CraftMapRenderer extends MapRenderer
{
    private final MapData worldMap;
    
    public CraftMapRenderer(final CraftMapView mapView, final MapData worldMap) {
        super(false);
        this.worldMap = worldMap;
    }
    
    @Override
    public void render(final MapView map, final MapCanvas canvas, final Player player) {
        for (int x = 0; x < 128; ++x) {
            for (int y = 0; y < 128; ++y) {
                canvas.setPixel(x, y, this.worldMap.colors[y * 128 + x]);
            }
        }
        final MapCursorCollection cursors = canvas.getCursors();
        while (cursors.size() > 0) {
            cursors.removeCursor(cursors.getCursor(0));
        }
        for (final Object key : this.worldMap.mapDecorations.keySet()) {
            final Player other = Bukkit.getPlayerExact((String)key);
            if (other != null && !player.canSee(other)) {
                continue;
            }
            final Vec4b decoration = this.worldMap.mapDecorations.get(key);
            cursors.addCursor(decoration.getX(), decoration.getY(), (byte)(decoration.getRotation() & 0xF), decoration.getType());
        }
    }
}
