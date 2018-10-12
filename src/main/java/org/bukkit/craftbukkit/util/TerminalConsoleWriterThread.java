// 
// Decompiled by Procyon v0.5.30
// 

package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.OutputStream;
//import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;

import com.mojang.util.QueueLogAppender;

import jline.console.ConsoleReader;

public class TerminalConsoleWriterThread implements Runnable
{
    private final static String RESET_LINE=String.valueOf(jline.console.ConsoleReader.RESET_LINE);
    
    private final ConsoleReader reader;
    private final OutputStream output;
    
    public TerminalConsoleWriterThread(final OutputStream output, final ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }
    
    @Override
    public void run() {
        while (true) {
            final String message = QueueLogAppender.getNextLogEvent("TerminalConsole");
            if (message == null) {
                continue;
            }
            try {
                if (Main.useJline) {
                    this.output.write(RESET_LINE.getBytes());
                    this.output.write(ColouredConsoleSender.toAnsiStr(message).getBytes());
                    this.output.flush();
                    try {
                        this.reader.drawLine();
                    }
                    catch (Throwable t) {
                        this.reader.getCursorBuffer().clear();
                    }
                    this.reader.flush();
                }
                else {
                    this.output.write(message.getBytes());
                    this.output.flush();
                }
            }
            catch (IOException ex) {
                Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
