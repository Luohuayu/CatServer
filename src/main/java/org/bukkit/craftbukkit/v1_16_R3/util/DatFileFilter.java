package org.bukkit.craftbukkit.v1_16_R3.util;

import java.io.File;
import java.io.FilenameFilter;

public class DatFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".dat");
    }
}
