// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit;

import java.util.Arrays;
import java.io.IOException;
import java.io.OutputStream;
import net.minecraft.server.MinecraftServer;
import org.fusesource.jansi.AnsiConsole;

import jline.UnsupportedTerminal;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.List;

public class Main
{
    public static boolean useJline;
    public static boolean useConsole;
    
    static {
        Main.useJline = true;
        Main.useConsole = true;
    }
    
    public static OptionSet main(final String[] args) {
        final OptionParser parser = new OptionParser() {
            {
                this.acceptsAll(asList(new String[] { "?", "help" }), "Show the help");
                this.acceptsAll(asList(new String[] { "c", "config" }), "Properties file to use").withRequiredArg().ofType(File.class).defaultsTo(new File("server.properties"), new File[0]).describedAs("Properties file");
                this.acceptsAll(asList(new String[] { "P", "plugins" }), "Plugin directory to use").withRequiredArg().ofType(File.class).defaultsTo(new File("plugins"), new File[0]).describedAs("Plugin directory");
                this.acceptsAll(asList(new String[] { "h", "host", "server-ip" }), "Host to listen on").withRequiredArg().ofType(String.class).describedAs("Hostname or IP");
                this.acceptsAll(asList(new String[] { "W", "world-dir", "universe", "world-container" }), "World container").withRequiredArg().ofType(File.class).describedAs("Directory containing worlds");
                this.acceptsAll(asList(new String[] { "w", "world", "level-name" }), "World name").withRequiredArg().ofType(String.class).describedAs("World name");
                this.acceptsAll(asList(new String[] { "p", "port", "server-port" }), "Port to listen on").withRequiredArg().ofType(Integer.class).describedAs("Port");
                this.acceptsAll(asList(new String[] { "o", "online-mode" }), "Whether to use online authentication").withRequiredArg().ofType(Boolean.class).describedAs("Authentication");
                this.acceptsAll(asList(new String[] { "s", "size", "max-players" }), "Maximum amount of players").withRequiredArg().ofType(Integer.class).describedAs("Server size");
                this.acceptsAll(asList(new String[] { "d", "date-format" }), "Format of the date to display in the console (for log entries)").withRequiredArg().ofType(SimpleDateFormat.class).describedAs("Log date format");
                this.acceptsAll(asList(new String[] { "log-pattern" }), "Specfies the log filename pattern").withRequiredArg().ofType(String.class).defaultsTo("server.log", new String[0]).describedAs("Log filename");
                this.acceptsAll(asList(new String[] { "log-limit" }), "Limits the maximum size of the log file (0 = unlimited)").withRequiredArg().ofType(Integer.class).defaultsTo(0, new Integer[0]).describedAs("Max log size");
                this.acceptsAll(asList(new String[] { "log-count" }), "Specified how many log files to cycle through").withRequiredArg().ofType(Integer.class).defaultsTo(1, new Integer[0]).describedAs("Log count");
                this.acceptsAll(asList(new String[] { "log-append" }), "Whether to append to the log file").withRequiredArg().ofType(Boolean.class).defaultsTo(true, new Boolean[0]).describedAs("Log append");
                this.acceptsAll(asList(new String[] { "log-strip-color" }), "Strips color codes from log file");
                this.acceptsAll(asList(new String[] { "b", "bukkit-settings" }), "File for bukkit settings").withRequiredArg().ofType(File.class).defaultsTo(new File("bukkit.yml"), new File[0]).describedAs("Yml file");
                this.acceptsAll(asList(new String[] { "C", "commands-settings" }), "File for command settings").withRequiredArg().ofType(File.class).defaultsTo(new File("commands.yml"), new File[0]).describedAs("Yml file");
                this.acceptsAll(asList(new String[] { "nojline" }), "Disables jline and emulates the vanilla console");
                this.acceptsAll(asList(new String[] { "noconsole" }), "Disables the console");
                this.acceptsAll(asList(new String[] { "v", "version" }), "Show the CraftBukkit Version");
                this.acceptsAll(asList(new String[] { "demo" }), "Demo mode");
                this.acceptsAll(asList(new String[] { "S", "spigot-settings" }), "File for spigot settings").withRequiredArg().ofType(File.class).defaultsTo(new File("spigot.yml"), new File[0]).describedAs("Yml file");
            }
        };
        OptionSet options = null;
        try {
            options = parser.parse(args);
        }
        catch (OptionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }
        if (options != null) {
            if (!options.has("?")) {
                //if (options.has("v")) {
                //    System.out.println(CraftServer.class.getPackage().getImplementationVersion());
                //    return null;
                //}
                final String path = new File(".").getAbsolutePath();
                if (path.contains("!") || path.contains("+")) {
                    System.err.println("Cannot run server in a directory with ! or + in the pathname. Please rename the affected folders and try again.");
                    return null;
                }
                try {
                    final String jline_UnsupportedTerminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 'U', 'n', 's', 'u', 'p', 'p', 'o', 'r', 't', 'e', 'd', 'T', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
                    final String jline_terminal = new String(new char[] { 'j', 'l', 'i', 'n', 'e', '.', 't', 'e', 'r', 'm', 'i', 'n', 'a', 'l' });
                    Main.useJline = !jline_UnsupportedTerminal.equals(System.getProperty(jline_terminal));
                    if (options.has("nojline")) {
                        System.setProperty("user.language", "en");
                        Main.useJline = false;
                    }
                    if (Main.useJline) {
                        AnsiConsole.systemInstall();
                    }
                    else {
                        System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", UnsupportedTerminal.class.getName());
                    }
                    if (options.has("noconsole")) {
                        Main.useConsole = false;
                    }
                    //System.out.println("Loading libraries, please wait...");
                    //MinecraftServer.main(options);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
                return options;
            }
        }
        try {
            parser.printHelpOn(System.out);
        }
        catch (IOException ex2) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return null;
    }
    
    private static List<String> asList(final String... params) {
        return Arrays.asList(params);
    }
}
