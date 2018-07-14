// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread
{
    private final MinecraftServer server;
    
    public ServerShutdownThread(final MinecraftServer server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        //try {
        try {
            this.server.stopServer();
        } catch (ExceptionWorldConflict exceptionWorldConflict) {
            exceptionWorldConflict.printStackTrace();
        }
        //}
        /*catch (MinecraftException ex) {
            ex.printStackTrace();
            try {
                this.server.reader.getTerminal().restore();
            }
            catch (Exception ex2) {}
            return;
        }
        finally {
            try {
                this.server.reader.getTerminal().restore();
            }
            catch (Exception ex3) {}
        }
        try {
            this.server.reader.getTerminal().restore();
        }
        catch (Exception ex4) {}*/
    }
}
