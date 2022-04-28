package org.bukkit.craftbukkit.v1_16_R3.util;

import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.close();
        } finally {
            try {
                net.minecrell.terminalconsole.TerminalConsoleAppender.close();
            } catch (Exception e) {
            }
        }
    }
}
