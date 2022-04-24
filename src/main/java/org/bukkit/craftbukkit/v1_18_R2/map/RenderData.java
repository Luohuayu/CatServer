package org.bukkit.craftbukkit.v1_18_R2.map;

import java.util.ArrayList;
import org.bukkit.map.MapCursor;

public class RenderData {

    public byte[] buffer;
    public final ArrayList<MapCursor> cursors;

    public RenderData() {
        this.buffer = new byte[128 * 128];
        this.cursors = new ArrayList<MapCursor>();
    }

}
