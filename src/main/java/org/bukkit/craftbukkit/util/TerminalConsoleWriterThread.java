package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;

import com.mojang.util.QueueLogAppender;

import jline.console.ConsoleReader;

public class TerminalConsoleWriterThread implements Runnable {

    private final static byte[] RESET_LINE = String.valueOf(jline.console.ConsoleReader.RESET_LINE).getBytes();

    final private ConsoleReader reader;
    final private OutputStream output;

    public TerminalConsoleWriterThread(OutputStream output, ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }

    public void run() {
        String message;

        // Using name from log4j config in vanilla jar
        while (true) {
            message = QueueLogAppender.getNextLogEvent("TerminalConsole");
            if (message == null) {
                continue;
            }

            try {
                if (Main.useJline) {
                    this.output.write(RESET_LINE);
                    this.output.write(ColouredConsoleSender.toAnsiStr(message).getBytes());
                    this.output.flush();

                    try {
                        reader.drawLine();
                    } catch (Throwable ex) {
                        reader.getCursorBuffer().clear();
                    }
                    reader.flush();
                } else {
                    output.write(message.getBytes());
                    output.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
