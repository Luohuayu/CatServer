// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.io.File;
import java.io.FilenameFilter;

public class DatFileFilter implements FilenameFilter
{
    @Override
    public boolean accept(final File dir, final String name) {
        return name.endsWith(".dat");
    }
}
