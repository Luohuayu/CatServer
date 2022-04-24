package org.bukkit.craftbukkit.v1_18_R2.util;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {

        // FORGE: Halting as GameTestServer will cause issues as it always calls System#exit on both crash and normal exit, so skip it
        if (!(server instanceof net.minecraft.gametest.framework.GameTestServer))
            server.halt(true);
        LogManager.shutdown(); // we're manually managing the logging shutdown on the server. Make sure we do it here at the end.

        try {
            org.spigotmc.AsyncCatcher.enabled = false; // Spigot
            server.close();
        } finally {
            try {
                server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
