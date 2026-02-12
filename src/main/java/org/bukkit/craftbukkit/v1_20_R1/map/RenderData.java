package org.bukkit.craftbukkit.v1_20_R1.map;

import java.util.ArrayList;
import org.bukkit.map.MapCursor;

public class RenderData {

    public final byte[] buffer;
    public final ArrayList<MapCursor> cursors;

    public RenderData() {
        this.buffer = new byte[128 * 128];
        this.cursors = new ArrayList<MapCursor>();
    }

}
