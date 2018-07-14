// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.map;

import org.bukkit.map.MapView;
import org.bukkit.map.MapFont;
import java.awt.image.ImageObserver;
import org.bukkit.map.MapPalette;
import java.awt.Image;
import java.util.Arrays;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapCanvas;

public class CraftMapCanvas implements MapCanvas
{
    private final byte[] buffer;
    private final CraftMapView mapView;
    private byte[] base;
    private MapCursorCollection cursors;
    
    protected CraftMapCanvas(final CraftMapView mapView) {
        this.buffer = new byte[16384];
        this.cursors = new MapCursorCollection();
        this.mapView = mapView;
        Arrays.fill(this.buffer, (byte)(-1));
    }
    
    @Override
    public CraftMapView getMapView() {
        return this.mapView;
    }
    
    @Override
    public MapCursorCollection getCursors() {
        return this.cursors;
    }
    
    @Override
    public void setCursors(final MapCursorCollection cursors) {
        this.cursors = cursors;
    }
    
    @Override
    public void setPixel(final int x, final int y, final byte color) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) {
            return;
        }
        if (this.buffer[y * 128 + x] != color) {
            this.buffer[y * 128 + x] = color;
            this.mapView.worldMap.updateMapData(x, y);
        }
    }
    
    @Override
    public byte getPixel(final int x, final int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) {
            return 0;
        }
        return this.buffer[y * 128 + x];
    }
    
    @Override
    public byte getBasePixel(final int x, final int y) {
        if (x < 0 || y < 0 || x >= 128 || y >= 128) {
            return 0;
        }
        return this.base[y * 128 + x];
    }
    
    protected void setBase(final byte[] base) {
        this.base = base;
    }
    
    protected byte[] getBuffer() {
        return this.buffer;
    }
    
    @Override
    public void drawImage(final int x, final int y, final Image image) {
        final byte[] bytes = MapPalette.imageToBytes(image);
        for (int x2 = 0; x2 < image.getWidth(null); ++x2) {
            for (int y2 = 0; y2 < image.getHeight(null); ++y2) {
                this.setPixel(x + x2, y + y2, bytes[y2 * image.getWidth(null) + x2]);
            }
        }
    }
    
    @Override
    public void drawText(int x, int y, final MapFont font, final String text) {
        final int xStart = x;
        byte color = 44;
        if (!font.isValid(text)) {
            throw new IllegalArgumentException("text contains invalid characters");
        }
        for (int i = 0; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            if (ch == '\n') {
                x = xStart;
                y += font.getHeight() + 1;
            }
            else {
                if (ch == '§') {
                    final int j = text.indexOf(59, i);
                    if (j >= 0) {
                        try {
                            color = Byte.parseByte(text.substring(i + 1, j));
                            i = j;
                            continue;
                        }
                        catch (NumberFormatException ex) {}
                    }
                }
                final MapFont.CharacterSprite sprite = font.getChar(text.charAt(i));
                for (int r = 0; r < font.getHeight(); ++r) {
                    for (int c = 0; c < sprite.getWidth(); ++c) {
                        if (sprite.get(r, c)) {
                            this.setPixel(x + c, y + r, color);
                        }
                    }
                }
                x += sprite.getWidth() + 1;
            }
        }
    }
}
