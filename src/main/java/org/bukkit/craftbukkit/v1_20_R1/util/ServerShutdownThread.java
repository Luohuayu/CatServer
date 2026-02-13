package org.bukkit.craftbukkit.v1_20_R1.util;

import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            org.spigotmc.AsyncCatcher.enabled = false; // Spigot
            server.close();
        } finally {
            try {
                net.minecrell.terminalconsole.TerminalConsoleAppender.close(); // CatServer
            } catch (Exception e) {
            }
        }
    }
}
