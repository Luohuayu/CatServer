// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import java.util.Iterator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;
import java.io.Writer;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Arrays;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import java.io.StringWriter;
import net.minecraft.crash.ICrashReportDetail;

public class CraftCrashReport implements ICrashReportDetail<Object>
{
    @Override
    public Object call() throws Exception {
        final StringWriter value = new StringWriter();
        try {
            value.append("\n   Running: ").append(Bukkit.getName()).append(" version ").append(Bukkit.getVersion()).append(" (Implementing API version ").append(Bukkit.getBukkitVersion()).append(") ").append(String.valueOf(MinecraftServer.getServerInst().isServerInOnlineMode()));
            value.append("\n   Plugins: {");
            Plugin[] plugins;
            for (int length = (plugins = Bukkit.getPluginManager().getPlugins()).length, i = 0; i < length; ++i) {
                final Plugin plugin = plugins[i];
                final PluginDescriptionFile description = plugin.getDescription();
                value.append(' ').append(description.getFullName()).append(' ').append(description.getMain()).append(' ').append(Arrays.toString(description.getAuthors().toArray())).append(',');
            }
            value.append("}\n   Warnings: ").append(Bukkit.getWarningState().name());
            value.append("\n   Reload Count: ").append(String.valueOf(MinecraftServer.getServerInst().server.reloadCount));
            value.append("\n   Threads: {");
            for (final Map.Entry<Thread, ? extends Object[]> entry : Thread.getAllStackTraces().entrySet()) {
                value.append(' ').append(entry.getKey().getState().name()).append(' ').append(entry.getKey().getName()).append(": ").append(Arrays.toString((Object[])(Object)entry.getValue())).append(',');
            }
            value.append("}\n   ").append(Bukkit.getScheduler().toString());
        }
        catch (Throwable t) {
            value.append("\n   Failed to handle CraftCrashReport:\n");
            final PrintWriter writer = new PrintWriter(value);
            t.printStackTrace(writer);
            writer.flush();
        }
        return value.toString();
    }
}
